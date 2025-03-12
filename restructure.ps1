$sourceDir = "src/main/java/com/zidioDev/ExpenseManager"
$baseDir = "backend/ExpenseManager"

# Create new directory structure
$dirs = @(
    "config",
    "domain/entity",
    "domain/enums",
    "service/impl",
    "service/auth",
    "dto",
    "repository",
    "controller",
    "util",
    "security"
)

# Create directories
foreach ($dir in $dirs) {
    $path = Join-Path $baseDir $sourceDir $dir
    if (!(Test-Path $path)) {
        New-Item -ItemType Directory -Path $path -Force
    }
}

# File mapping (source -> destination)
$fileMapping = @{
    "config/AuthConfig.java" = @{
        "source" = "AuthConfig.java"
        "package" = "package com.zidioDev.ExpenseManager.config;"
    }
    "config/ApprovalConfig.java" = @{
        "source" = "ApprovalConfig.java"
        "package" = "package com.zidioDev.ExpenseManager.config;"
    }
    "domain/entity/User.java" = @{
        "source" = "User.java"
        "package" = "package com.zidioDev.ExpenseManager.domain.entity;"
    }
    "domain/entity/Expense.java" = @{
        "source" = "Expense.java"
        "package" = "package com.zidioDev.ExpenseManager.domain.entity;"
    }
    "domain/entity/FileAttachment.java" = @{
        "source" = "FileAttachment.java"
        "package" = "package com.zidioDev.ExpenseManager.domain.entity;"
    }
    "domain/entity/UserPrincipal.java" = @{
        "source" = "UserPrincipal.java"
        "package" = "package com.zidioDev.ExpenseManager.domain.entity;"
    }
    "domain/enums/AuthProvider.java" = @{
        "source" = "AuthProvider.java"
        "package" = "package com.zidioDev.ExpenseManager.domain.enums;"
    }
    "service/auth/OAuth2UserService.java" = @{
        "source" = "OAuth2UserService.java"
        "package" = "package com.zidioDev.ExpenseManager.service.auth;"
    }
    "service/impl/ExpenseServiceImpl.java" = @{
        "source" = "ExpenseServiceImpl.java"
        "package" = "package com.zidioDev.ExpenseManager.service.impl;"
    }
}

# Process each file
foreach ($destPath in $fileMapping.Keys) {
    $mapping = $fileMapping[$destPath]
    $sourcePath = Join-Path $baseDir $sourceDir $mapping.source
    $destFullPath = Join-Path $baseDir $sourceDir $destPath
    
    if (Test-Path $sourcePath) {
        # Read the file content
        $content = Get-Content $sourcePath -Raw
        
        # Replace the package declaration
        $content = $content -replace "package com\.zidioDev\.ExpenseManager(\.[a-zA-Z]+)*;", $mapping.package
        
        # Update import statements
        $content = $content -replace "import com\.zidioDev\.ExpenseManager\.", "import com.zidioDev.ExpenseManager.domain.entity."
        
        # Create the destination directory if it doesn't exist
        $destDir = Split-Path $destFullPath
        if (!(Test-Path $destDir)) {
            New-Item -ItemType Directory -Path $destDir -Force
        }
        
        # Write the modified content to the new location
        Set-Content -Path $destFullPath -Value $content
        
        # Remove the original file
        Remove-Item $sourcePath
    }
}

Write-Host "Codebase restructuring completed!" 