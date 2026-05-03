$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$port = 8082

function Resolve-AppSecret {
    if ($env:APP_WECHAT_APP_SECRET) {
        return $env:APP_WECHAT_APP_SECRET
    }

    $userValue = [System.Environment]::GetEnvironmentVariable("APP_WECHAT_APP_SECRET", "User")
    if ($userValue) {
        $env:APP_WECHAT_APP_SECRET = $userValue
        return $userValue
    }

    $machineValue = [System.Environment]::GetEnvironmentVariable("APP_WECHAT_APP_SECRET", "Machine")
    if ($machineValue) {
        $env:APP_WECHAT_APP_SECRET = $machineValue
        return $machineValue
    }

    return $null
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

if (-not (Resolve-AppSecret)) {
    Write-Host ""
    Write-Host "缺少 APP_WECHAT_APP_SECRET。" -ForegroundColor Red
    Write-Host "请先在 Windows 用户环境变量里设置小程序密钥，然后重新运行本脚本。" -ForegroundColor Yellow
    Write-Host ""
    Write-Host '[System.Environment]::SetEnvironmentVariable("APP_WECHAT_APP_SECRET", "你的小程序AppSecret", "User")' -ForegroundColor Cyan
    exit 1
}

$portEntries = Test-PortInUse -TargetPort $port
if ($portEntries.Count -gt 0) {
    Write-Host ""
    Write-Host "检测到端口 $port 已被占用，请先关闭旧进程后再启动：" -ForegroundColor Yellow
    $portEntries | Format-Table -AutoSize
    Write-Host ""
    Write-Host "如果是旧后端进程，最省事的方式是打开任务管理器结束对应 java 进程。" -ForegroundColor Yellow
    exit 1
}

Set-Location $projectRoot
Write-Host "启动本地后端，端口 $port ..." -ForegroundColor Green
mvn "-Dmaven.repo.local=.m2repo" spring-boot:run
