$java = "C:\Program Files\Java\jdk-17\bin\java.exe"
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$out  = "$root\target\classes"

# Recompile
Write-Host "Compiling..." -ForegroundColor Cyan
$javac = "C:\Program Files\Java\jdk-17\bin\javac.exe"
New-Item -ItemType Directory -Path $out -Force | Out-Null
& $javac -d $out (Get-ChildItem "$root\src\main\java\*.java").FullName
if ($LASTEXITCODE -ne 0) { Write-Host "Compile failed" -ForegroundColor Red; exit 1 }
Write-Host "Compiled OK" -ForegroundColor Green

# Launch (working dir set so web/ path resolves)
Write-Host "Starting server at http://localhost:8080 ..." -ForegroundColor Cyan
Set-Location $root
& $java -cp $out WebLauncher
