package com.crackers.vinayakatraders.service;

import com.crackers.vinayakatraders.dto.InventoryDto;
import com.crackers.vinayakatraders.dto.InventoryListResponse;
import com.crackers.vinayakatraders.dto.InventoryRequest;
import com.crackers.vinayakatraders.dto.UpdateInventoryRequest;
import com.crackers.vinayakatraders.entity.Inventory;
import com.crackers.vinayakatraders.entity.InventoryState;
import com.crackers.vinayakatraders.exception.InvalidInventoryStateTransitionException;
import com.crackers.vinayakatraders.exception.InventoryNotFoundException;
import com.crackers.vinayakatraders.repository.InventoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class InventoryService {
    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void addItem(InventoryRequest request) {
        // Use checked arithmetic to avoid silently overflowing a Java int subTotal.
        int subtotal = Math.multiplyExact(request.pricePerCarton(), request.numOfCartons());
        short stateCode = InventoryState.fromApiValue(request.state()).code();
        repository.save(new Inventory(
                request.brandOrCompany(),
                stateCode,
                request.item(),
                request.numOfBoxes(),
                request.numOfCartons(),
                request.pricePerCarton(),
                subtotal
        ));
    }

    public InventoryListResponse getInventory(int pageNumber, int pageSize, String title, String stateValue) {
        Short state = (stateValue == null || stateValue.isBlank())
                ? null : InventoryState.fromApiValue(stateValue).code();
        int safePage = pageNumber <= 0 ? 0 : pageNumber - 1;
        int safeSize = pageSize <= 0 || pageSize > 100 ? 10 : pageSize;
        String safeTitle = title == null ? "" : title.trim();

        // Each task performs its own Spring Data repository call. Transactions are thread-bound,
        // so do not rely on a transaction opened on the request thread being shared with these tasks.
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<Page<Inventory>> pageFuture = executor.submit(() ->
                    repository.findFiltered(state, safeTitle, PageRequest.of(safePage, safeSize)));
            Future<Long> sumFuture = executor.submit(() -> repository.sumSubtotalFiltered(state, safeTitle));

            Page<Inventory> page = pageFuture.get();
            Long sum = sumFuture.get();
            long total = sum == null ? 0L : sum;
            List<InventoryDto> items = page.getContent().stream().map(InventoryDto::from).toList();
            return new InventoryListResponse(items, total, page.getTotalElements());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Inventory query was interrupted", ex);
        } catch (ExecutionException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            throw new IllegalStateException("Failed to retrieve inventory", cause);
        }
    }

    @Transactional
    public void updateInventory(UpdateInventoryRequest request) {
        Inventory existing = repository.findById(request.id())
                .orElseThrow(InventoryNotFoundException::new);

        InventoryState currentState = InventoryState.fromCode(existing.getState());

        if (currentState != InventoryState.ORDERED && currentState != InventoryState.RECEIVED) {
            throw new InvalidInventoryStateTransitionException();
        }

        InventoryState nextState =
                currentState == InventoryState.ORDERED
                        ? InventoryState.RECEIVED
                        : InventoryState.UNPACKED;

        int requestedCartons = request.numOfCartons();
        int requestedSubtotal =
                requestedCartons * existing.getPricePerCarton();

        if (existing.getNumOfCartons() == requestedCartons) {
            // Full match: update the existing record only.
            existing.setState(nextState.code());
            existing.setSubTotal(
                    existing.getNumOfCartons() * existing.getPricePerCarton());

            repository.save(existing);

        } else {
            // Partial transition: preserve the remaining quantity
            // and create a separate record for the transitioned quantity.
            int difference = existing.getNumOfCartons() - requestedCartons;
            int remainingSubtotal = difference * existing.getPricePerCarton();

            existing.setNumOfCartons(difference);
            existing.setSubTotal(remainingSubtotal);

            repository.save(existing);

            Inventory newRecord = new Inventory(
                    request.brandOrCompany(),
                    nextState.code(),
                    request.item(),
                    request.numOfBoxes(),
                    requestedCartons,
                    request.pricePerCarton(),
                    requestedSubtotal
            );

            repository.save(newRecord);
        }
    }
}
