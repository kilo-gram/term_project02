import java.util.*;
import java.util.List;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class IDE {
    private String Filename;
    private boolean isCompile;
    Scanner scanner = new Scanner(System.in);

    public IDE() {
        Filename = null;
        isCompile = false;
    }

    public void Menu() {
        System.out.println("## ## ## ## ## ## ## ## ## ## ## #");
        System.out.println("1. 자바 파일 업로드");
        System.out.println("2. 컴파일");
        System.out.println("3. 런");
        System.out.println("4. 파일 삭제");
        System.out.println("5. 컴파일 오류 파일");
        System.out.println("6. 프로그램 종료");
        System.out.print("Choice : ");
    }

    public void run() {

        while (true) {
            Menu();
            int chioce = scanner.nextInt();
            scanner.nextLine();

            switch (chioce) {
                case 1: {
                    System.out.println("#######################");
                    System.out.println("자바 파일 이름: ");
                    Filename = scanner.nextLine();
                    if (!Filename.endsWith(".java")) {
                        Filename += ".java";
                        System.out.println("확장자를 추가: " + Filename);
                    }

                    isCompile = false;
                    break;
                }
                case 2: {
                    System.out.println("#######################");
                    if (Filename == null) {
                        System.out.println("파일이 업로드 되지 않음.");
                    } else {
                        File_Compile();
                    }
                    break;

                }
                case 3: {
                    System.out.println("#######################");
                    if (Filename == null) {
                        System.out.println("파일이 업로드 되지 않음. ");
                    } else if (!isCompile) {
                        System.out.println("컴파일 에러 - 실행 불가 ");
                    } else {
                        File_run();
                    }
                    break;
                }
                case 4: {
                    System.out.println("#######################");
                    Filename = null;
                    isCompile = false;
                    System.out.println("파일 삭제 완료");

                }
                case 5: {
                    System.out.println("#######################");
                    if (Filename == null || !isCompile) {
                        System.out.println("컴파일 에러 파일 없음.");
                    } else {
                        Error_File();
                    }
                    break;
                }
                case 6: {
                    System.out.println("#######################");
                    System.out.println("프로그램 종료");
                    scanner.close();
                    System.exit(0);
                }
                default: {
                    System.out.println("잘못된 선택입니다. 1-6 사이의 숫자를 입력하세요.");
                    break;
                }
            }

        }
    }

    public void File_Compile() {
        String s;

        try {
            ProcessBuilder t = new ProcessBuilder("cd C:\\\\PS\\\\java recture\\\\teamproject && javac " + Filename);
            Process oProcess = t.start();

            BufferedReader stdOut = new BufferedReader(new InputStreamReader(oProcess.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(oProcess.getErrorStream()));

            boolean isiterror = false;
            while ((s = stdOut.readLine()) != null) {
                isiterror = true;
                System.out.println(s);
            }
            while ((s = stdError.readLine()) != null) {
                isiterror = true;
                System.out.println(s);
            }

            if (!isiterror) {
                System.out.println("compiled successfully");
            } else {
                System.out.println("3 compile error occurred –" + Filename + ".error ");
            }

        } catch (IOException e) {
            // TODO: handle exception
            System.err.println("에러! 외부 명령어 실행에 실패.\n" + e.getMessage());
        }
    }

    public void File_run() {
        try {
            String s;

            ProcessBuilder t = new ProcessBuilder("java", Filename);
            Process process = t.start();

            BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            while ((s = stdOut.readLine()) != null) {
                System.out.println(s);
            }
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

            System.out.println("Exit Code: " + process.exitValue());
            System.exit(process.exitValue());

        } catch (Exception e) {
            // TODO: handle exception
            System.err.println("에러! 파일 실행 실패\n" + e.getMessage());

        }
    }

    public void Error_File() {
        try {
            String Errorfile = Filename + ".error";

        } catch (IOException e) {
            // TODO: handle exception
            System.err.println("에러! 오류 파일 읽기 실패: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        IDE ide = new IDE();
        ide.run();

    }

}