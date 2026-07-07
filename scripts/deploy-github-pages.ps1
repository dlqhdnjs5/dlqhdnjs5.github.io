param(
  [string]$RepositoryUrl = "https://github.com/dlqhdnjs5/dlqhdnjs5.github.io.git"
)

$ErrorActionPreference = "Stop"

git branch -M master

$remote = git remote get-url origin 2>$null
if ($LASTEXITCODE -ne 0) {
  git remote add origin $RepositoryUrl
} elseif ($remote -ne $RepositoryUrl) {
  git remote set-url origin $RepositoryUrl
}

git push -u origin master

Write-Host "Push complete. In GitHub, set Settings > Pages > Source to GitHub Actions."
