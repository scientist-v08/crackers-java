# ================================================
# Robust Java 21 + Maven Setup for PowerShell
# ================================================

$env:JAVA_HOME = "C:\Program Files\Microsoft\jdk-21.0.11.10-hotspot"
$env:M2_HOME   = "C:\Program Files\apache-maven-3.9.15"

$env:PATH = "$env:JAVA_HOME\bin;$env:M2_HOME\bin;$env:PATH"

Write-Host "✅ Java 21 + Maven successfully configured for this session" -ForegroundColor Green
Write-Host "JAVA_HOME → $env:JAVA_HOME" -ForegroundColor Cyan

java --version
mvn --version