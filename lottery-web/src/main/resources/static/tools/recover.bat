@echo off
@color 0a 
title ���������
echo *******************************************************************
echo *      �ó���Ϊ������³���,�����ȫ�����������������ִ��      *
echo *******************************************************************
rem if not exist C:\Windows\System32\drivers\etc\hosts1  echo ...��&&Pause >nul&&Exit 
xcopy C:\Windows\System32\drivers\etc\hosts1\hosts C:\Windows\System32\drivers\etc\hosts /y
echo ��ԭ [��������] ���~
echo.
echo �˳���.....
ping /n 3 127.1>nul
