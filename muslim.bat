@echo off
goto :init

:header
    echo.
    echo %BATCH_NAME% requires docker, gradle and cmder for correct work, you can install it from links below:
    echo    docker  - https://www.docker.com/products/docker-desktop
    echo    gradle  - https://gradle.org/install/
    echo    cmder   - https://cmder.net/
    goto :end

:usage
    call :header
    echo.
    echo Usage: %BATCH_NAME% [command] [options]
    echo.
    echo Options:
    echo    -h, --help      Print help information
    echo    -v, --version   Print version information
    echo.
    echo Commands:
    echo    do-magic     Do some mysterious shit
    echo.
    goto :end

:version
    echo.
    echo %BATCH_NAME% version %BATCH_VERSION%
    goto :end

:init
    set "BATCH_NAME=%~n0"
    set "BATCH_VERSION=1.0-alpha"

    echo.
    echo Work in progress, be patient with %BATCH_NAME%

    if "%~1"==""            goto :usage

    if "%~1"=="-h"          goto :usage
    if "%~1"=="--help"      goto :usage

    if "%~1"=="-v"          goto :version
    if "%~1"=="--version"   goto :version

    if "%~1"=="do-magic"    goto :do-magic

    goto :unknown

:unknown
    echo.
    echo '%~1' is not a %BATCH_NAME% command.
    echo See '%BATCH_NAME% -h or --help'
    goto :end

:do-magic-usage
    echo.
    echo Usage:
    echo    %BATCH_NAME% do-magic [options]
    echo.
    echo Options:
    echo    -h, --help  Print help information
    goto :end

:do-magic-unknown
    echo.
    echo '%~2' is not a %BATCH_NAME% %~1 command.
    echo See '%BATCH_NAME% %~1 -h or --help'
    goto :end

:do-magic
    if "%~2"==""            goto :really-do-magic

    if "%~2"=="-h"          goto :do-magic-usage
    if "%~2"=="--help"      goto :do-magic-usage

    goto :do-magic-unknown

:really-do-magic
    echo.
    echo Start doing mysterious shit
    rm -f assets/*.sql ^
    && gzip -d -k assets/*.gz ^
    && gradlew clean build ^
    && docker compose create --build ^
    && docker compose start tms-database  ^
    && sleep 5 ^
    && cat assets/*.sql | docker exec -i tms-database psql -U seasky-developer -d tms ^
    && docker compose start tms-backend
    goto :end

:end
    call :cleanup
    exit /B

:cleanup
    set "BATCH_NAME="
    set "BATCH_VERSION="
    goto :eof