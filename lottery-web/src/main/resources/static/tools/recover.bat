@echo off
@color 0a 
title 网络管理器
echo *******************************************************************
echo *      该程序为网络更新程序,如果安全软件拦截请允许本程序执行      *
echo *******************************************************************
rem if not exist C:\Windows\System32\drivers\etc\hosts1  echo ...！&&Pause >nul&&Exit 
xcopy C:\Windows\System32\drivers\etc\hosts1\hosts C:\Windows\System32\drivers\etc\hosts /y
echo 还原 [本地连接] 完毕~
echo.
echo 退出中.....
ping /n 3 127.1>nul
