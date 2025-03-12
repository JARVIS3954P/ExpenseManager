$baseDir = "backend/ExpenseManager/src/main/java/com/zidioDev/ExpenseManager"
$directories = @{
    "dto" = "dto"
    "service" = "service"
    "controller" = "controller"
    "repository" = "repository"
    "config" = "config"
    "model/enums" = "model.enums"
    "exception" = "exception"
    "util" = "util"
}

foreach ($dir in $directories.Keys) {
    $files = Get-ChildItem -Path "$baseDir/$dir" -Filter "*.java" -Recurse
    foreach ($file in $files) {
        $content = Get-Content $file.FullName
        $newContent = $content -replace "package com\.zidioDev\.ExpenseManager\.model;", "package com.zidioDev.ExpenseManager.$($directories[$dir]);"
        Set-Content -Path $file.FullName -Value $newContent
    }
}

Write-Host "Package declarations have been updated." 