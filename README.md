### SSD.java
이 프로그램은 nand.txt라는 파일을 이용해 특정 블록에 데이터를 읽고 쓰는 기능을 제공합니다.    
명령어는 두 가지로, "읽기"와 "쓰기"로 구분되어 있습니다. 프로그램을 실행할 때 R을 입력하면 "읽기", W를 입력하면 "쓰기" 작업을 수행합니다.

##### 1. main 메서드   
프로그램이 실행되면 args라는 인자를 통해 사용자의 입력을 받아옵니다. 이때, 최소 2개의 인자가 전달되어야만 프로그램이 실행됩니다.   
첫 번째 인자는 mode입니다. R이면 "읽기", W이면 "쓰기" 작업을 수행합니다.   
두 번째 인자는 블록 번호를 의미하며, 0에서 99 사이의 값을 받아야 합니다. 이 범위를 벗어나면 오류 메시지가 출력됩니다.    
"쓰기" 작업인 경우, 세 번째 인자로 입력될 데이터가 필요합니다. 이 데이터는 16진수 4바이트 형식인 0x로 시작하고 8자리 숫자 또는 문자로 구성되어야 합니다. 이를 정규식 0x[0-9A-F]{8}을 이용하여 검증합니다. 만약 형식에 맞지 않으면 오류 메시지를 출력합니다.

##### 2. writeToFile 메서드    
"쓰기" 작업을 수행하는 함수로, nand.txt 파일에서 지정된 블록의 내용을 새 데이터로 바꿉니다.    
파일을 읽고, 변경할 블록 번호의 내용을 수정한 후, 다시 파일에 쓰는 방식으로 동작합니다.

##### 3. readFromFile 메서드    
이 메서드는 "읽기" 작업을 수행하며, 지정된 블록의 데이터를 result.txt 파일에 기록하는 기능을 담당합니다.   
nand.txt 파일을 읽으면서 지정된 블록을 찾으면 그 데이터를 result.txt에 추가하고 작업을 완료합니다.   

<hr/>

### TestShell.java

##### 1. main 메서드   
프로그램은 main 메서드에서 while 루프를 사용하여 사용자가 입력한 명령을 지속적으로 처리합니다.   
사용자가 SHELL > 프롬프트에서 명령을 입력하면 이를 처리하고 결과를 출력합니다.   
지원되는 명령어는 write, read, fullwrite, fullread, help, 그리고 exit입니다.

##### 2. 주요 명령어   
- write <address> <data>: write 명령어는 지정된 주소에 데이터 값을 쓰는 기능을 제공합니다. executeSSDCommand 메서드를 호출하여 SSD 프로그램을 통해 해당 블록에 데이터를 쓰게 됩니다.   
- read <address>: read 명령어는 지정된 주소의 데이터를 읽는 기능입니다. 역시 executeSSDCommand 메서드를 호출하여 SSD 프로그램을 통해 해당 데이터를 읽습니다.   
- fullwrite <data>: fullwrite 명령어는 nand.txt 파일의 모든 라인을 입력된 데이터 값으로 업데이트합니다. 이를 위해 fullWriteToFile 메서드를 호출합니다.   
- fullread: fullread 명령어는 nand.txt 파일의 모든 내용을 읽어서 화면에 출력하는 기능을 수행하며, fullReadToFile 메서드를 호출합니다.  
- help: help 명령어는 사용 가능한 모든 명령어를 화면에 출력합니다.  
- exit: exit 명령어는 프로그램을 종료합니다.

##### 3. executeSSDCommand 메서드    
이 메서드는 ProcessBuilder를 사용하여 SSD 프로그램을 실행하는 역할을 합니다.   
write 명령의 경우 java SSD W <block> <data> 형식으로 실행하고, read 명령의 경우 java SSD R <block> 형식으로 실행하여 다른 프로세스로 SSD 프로그램을 호출합니다.   
process.waitFor()를 사용하여 SSD 프로그램의 실행이 완료될 때까지 대기하며, 그 결과를 쉘에 출력합니다.    
  
##### 4. fullWriteToFile 메서드     
fullWriteToFile 메서드는 nand.txt 파일의 모든 줄을 지정된 데이터 값으로 업데이트하는 기능을 수행합니다.   
파일을 읽어 기존 내용을 지우고 새로운 데이터로 덮어씁니다.
 
5. fullReadToFile 메서드   
fullReadToFile 메서드는 nand.txt 파일의 모든 내용을 읽어서 콘솔에 출력하는 기능을 담당합니다.   
BufferedReader를 사용하여 파일의 각 줄을 읽고 화면에 출력합니다.   
