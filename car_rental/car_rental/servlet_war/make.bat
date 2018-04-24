set ARCHIVE=service.war
set CLASSES=service
set JAVAHOME="c:\Program Files\Java\jdk"
del %ARCHIVE%
%JAVAHOME%\bin\jar cvfM %ARCHIVE% -C %CLASSES% .

