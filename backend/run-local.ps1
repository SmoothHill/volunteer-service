$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$port = 8082

function Resolve-EnvValue {
    param(
        [string]$Name
    )

    if ($env:$Name) {
        return $env:$Name
    }

    foreach ($scope in @("User", "Machine")) {
        $value = [System.Environment]::GetEnvironmentVariable($Name, $scope)
        if ($value) {
            Set-Item -Path "Env:$Name" -Value $value
            return $value
        }
    }

    return $null
}

function Set-DefaultEnvValue {
    param(
        [string]$Name,
        [string]$Value
    )

    if (-not (Resolve-EnvValue -Name $Name)) {
        Set-Item -Path "Env:$Name" -Value $Value
    }
}

function Test-PortInUse {
    param(
        [int]$TargetPort
    )

    $lines = netstat -ano | Select-String ":$TargetPort"
    if (-not $lines) {
        return @()
    }

    $entries = @()
    foreach ($line in $lines) {
        $text = ($line.ToString() -replace "\s+", " ").Trim()
        $parts = $text.Split(" ")
        if ($parts.Length -lt 5) {
            continue
        }
        if ($parts[1] -notlike "*:$TargetPort") {
            continue
        }
        $entries += [PSCustomObject]@{
            Protocol = $parts[0]
            LocalAddress = $parts[1]
            State = $parts[3]
            Pid = [int]$parts[4]
        }
    }
    return $entries
}

Set-DefaultEnvValue -Name "PORT" -Value "$port"
Set-DefaultEnvValue -Name "APP_JWT_SECRET" -Value "local-dev-jwt-secret"
Set-DefaultEnvValue -Name "APP_SUPER_ADMIN_USERNAME" -Value "local-superadmin"
Set-DefaultEnvValue -Name "APP_SUPER_ADMIN_PASSWORD" -Value "LocalAdmin@123"
Set-DefaultEnvValue -Name "APP_SENSITIVE_ENCRYPTION_KEY" -Value "localdevkey12345"
Set-DefaultEnvValue -Name "APP_SENSITIVE_HASH_SALT" -Value "local-dev-salt"
Set-DefaultEnvValue -Name "APP_LOCAL_TEST_LOGIN_ENABLED" -Value "true"
Set-DefaultEnvValue -Name "APP_DEMO_DATA_ENABLED" -Value "true"
Set-DefaultEnvValue -Name "APP_CORS_ALLOWED_ORIGINS" -Value "http://127.0.0.1:5500,http://localhost:5500,http://127.0.0.1:5173,http://localhost:5173,http://127.0.0.1:4173,http://localhost:4173"

$wechatSecret = Resolve-EnvValue -Name "APP_WECHAT_APP_SECRET"
if (-not $wechatSecret) {
    Write-Host ""
    Write-Host "未检测到 APP_WECHAT_APP_SECRET，本次仍会启动本地后端。" -ForegroundColor Yellow
    Write-Host "微信授权登录和微信订阅消息会不可用，本地测试登录仍可使用。" -ForegroundColor Yellow
}

$portEntries = Test-PortInUse -TargetPort $port
if ($portEntries.Count -gt 0) {
    Write-Host ""
    Write-Host "检测到端口 $port 已被占用，请先关闭旧进程后再启动。" -ForegroundColor Yellow
    $portEntries | Format-Table -AutoSize
    Write-Host ""
    Write-Host "如果是旧后端进程，结束对应的 java 进程后再重试。" -ForegroundColor Yellow
    exit 1
}

Set-Location $projectRoot
Write-Host "本地超管账号: $env:APP_SUPER_ADMIN_USERNAME" -ForegroundColor Cyan
Write-Host "本地超管密码: $env:APP_SUPER_ADMIN_PASSWORD" -ForegroundColor Cyan
Write-Host "启动本地后端，端口 $port ..." -ForegroundColor Green
mvn "-Dmaven.repo.local=.m2repo" spring-boot:run
