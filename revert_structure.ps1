$sourceDir = "src/main/java/com/zidioDev/ExpenseManager"
$baseDir = "."

# Move all files from domain/entity back to model
Get-ChildItem -Path "$sourceDir/domain/entity" -Filter "*.java" | ForEach-Object {
    if (!(Test-Path "$sourceDir/model")) {
        New-Item -ItemType Directory -Path "$sourceDir/model" -Force
    }
    Move-Item $_.FullName -Destination "$sourceDir/model/" -Force
}

# Move enums back to model/enums
Get-ChildItem -Path "$sourceDir/domain/enums" -Filter "*.java" | ForEach-Object {
    if (!(Test-Path "$sourceDir/model/enums")) {
        New-Item -ItemType Directory -Path "$sourceDir/model/enums" -Force
    }
    Move-Item $_.FullName -Destination "$sourceDir/model/enums/" -Force
}

# Move security files back to config
Get-ChildItem -Path "$sourceDir/security" -Filter "*.java" | ForEach-Object {
    Move-Item $_.FullName -Destination "$sourceDir/config/" -Force
}

# Update package declarations back to original
Get-ChildItem -Path "$sourceDir" -Filter "*.java" -Recurse | ForEach-Object {
    $content = Get-Content $_.FullName -Raw
    
    # Revert domain.entity back to model
    $content = $content -replace "package com\.zidioDev\.ExpenseManager\.domain\.entity;", "package com.zidioDev.ExpenseManager.model;"
    
    # Revert domain.enums back to model.enums
    $content = $content -replace "package com\.zidioDev\.ExpenseManager\.domain\.enums;", "package com.zidioDev.ExpenseManager.model.enums;"
    
    # Update imports
    $content = $content -replace "import com\.zidioDev\.ExpenseManager\.domain\.entity\.", "import com.zidioDev.ExpenseManager.model."
    $content = $content -replace "import com\.zidioDev\.ExpenseManager\.domain\.enums\.", "import com.zidioDev.ExpenseManager.model.enums."
    
    Set-Content -Path $_.FullName -Value $content
}

# Remove empty directories
if (Test-Path "$sourceDir/domain") {
    Remove-Item "$sourceDir/domain" -Recurse -Force
}

Write-Host "Codebase structure reverted successfully!" 