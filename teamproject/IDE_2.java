import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.io.*;
// import java.nio.file.Files;

// 프론트 엔드
// FilePath -> 입력받은 경로
// FilePathSave -> 입력받은 경로 저장?
// Editing -> Edit화면 관리
// Result -> result화면 관리

// FilePath가 입력한 경로라고 치면은 이것을 IDE의 fullpath하고 filename으로
// 분리해서 각각 넣은 뒤에 나머지 잘 하면 될 것 같음
public class IDE_2 extends JFrame {
    public JTextField FilePath;
    public JTextField FilePathSave;
    public JTextArea Editing;
    public JTextArea Result;
    JButton Openbutton;
    JButton Savebutton;
    JButton Compilebutton;
    JButton Errorbutton;
    JButton Runbutton;
    JButton Deletebutton;
    JButton Clearbutton;

    IDE ide = new IDE();
    Backend be = new Backend();

    public IDE_2() {
        setTitle("Term_Project_2");
        setSize(1200, 1200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JButton Openbutton = new JButton("Open");
        JButton Savebutton = new JButton("Save");
        JButton Compilebutton = new JButton("Compile");
        JButton Errorbutton = new JButton("Save Error");
        JButton Runbutton = new JButton("Run");
        JButton Deletebutton = new JButton("Delete");
        JButton Clearbutton = new JButton("Clear");

        Openbutton.addActionListener(new file_action());
        Savebutton.addActionListener(new file_action());
        Compilebutton.addActionListener(new file_action());
        Errorbutton.addActionListener(new file_action());
        Runbutton.addActionListener(new file_action());
        Deletebutton.addActionListener(new file_action());
        Clearbutton.addActionListener(new file_action());

        // topPanel을 만들고 그 패널에 다른 패널을 만들어서 둘 곳을 저장한 후에
        // 그 패널들 다 topPanel에 넣음. 그리고 topPanel은 메인 패널에 넣음
        // 이건 더 구체화하면 더 좋을것 같은데
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel first_top_panel = new JPanel(new BorderLayout());
        JPanel second_top_panel = new JPanel(new BorderLayout());
        FilePath = new JTextField();
        FilePathSave = new JTextField();

        JPanel topButton = new JPanel();
        JPanel nextButton = new JPanel();
        topButton.add(Openbutton);
        nextButton.add(Savebutton);

        first_top_panel.add(FilePath, BorderLayout.CENTER);
        first_top_panel.add(topButton, BorderLayout.EAST);
        second_top_panel.add(FilePathSave, BorderLayout.CENTER);
        second_top_panel.add(nextButton, BorderLayout.EAST);

        topPanel.add(first_top_panel, BorderLayout.NORTH);
        topPanel.add(second_top_panel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        Editing = new JTextArea();
        Editing.setFont(new Font("Monospaced", Font.PLAIN, 24));
        JScrollPane editScroll = new JScrollPane(Editing);
        add(editScroll, BorderLayout.CENTER);

        JPanel bottomButtons = new JPanel();

        bottomButtons.add(Compilebutton);
        bottomButtons.add(Runbutton);
        bottomButtons.add(Errorbutton);
        bottomButtons.add(Deletebutton);
        bottomButtons.add(Clearbutton);

        Result = new JTextArea(8, 1);
        Result.setEditable(false);
        Result.setFont(new Font("Monospaced", Font.PLAIN, 24));
        Result.setBackground(new Color(245, 245, 245));
        JScrollPane resultScroll = new JScrollPane(Result);

        JPanel south = new JPanel(new BorderLayout());
        south.add(bottomButtons, BorderLayout.NORTH);
        south.add(resultScroll, BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);

        setVisible(true);
    }

    // 자바 파일 실행, 컴파일, 런, 에러 메세지를 cmd를 사용하여 실행할 수 있는 great한 class
    // orz
    class IDE {
        public String Filename;
        public boolean isCompileError;
        public String command;
        public String fullPath;
        public String s;

        public IDE() {
            fullPath = "";
            Filename = "";
            isCompileError = false;
        }

        // 자바 파일안의 소스코드를 출력하는 함수
        public void File_print() {
            try {
                // cd하고 && javac사이에 있는 건 파일 경로
                command = "cd /d " + fullPath + " && type " + Filename;

                ProcessBuilder t = new ProcessBuilder("cmd", "/c", command);
                Process oProcess = t.start();
                BufferedReader stdOut = new BufferedReader(new InputStreamReader(oProcess.getInputStream()));
                BufferedReader stdError = new BufferedReader(new InputStreamReader(oProcess.getErrorStream()));

                String input = "";
                boolean is_it_okay = false;

                while ((s = stdOut.readLine()) != null) {
                    is_it_okay = true;
                    input += s + "\n";
                }
                while ((s = stdError.readLine()) != null) {
                    input += s + "\n";
                }

                if (is_it_okay) {
                    // 파일 존재할 경우 - Editing 화면에 출력
                    Editing.setText(input);
                } else {
                    // 존재하지 않는 파일일 경우 - result화면에 오류 메세지 출력
                    Result.setText(input);
                }
                // Editing.getText();
            } catch (IOException e) {
                // TODO : handle exception
                Result.setText("에러! 파일 실행 실패\n" + e.getMessage());
            }
        }

        public void File_save() {
            try {
                String which_file;
                if (FilePathSave.getText().equals("")) {
                    which_file = Filename;
                } else {
                    which_file = FilePathSave.getText();
                }
                // cd하고 && javac사이에 있는 건 파일 경로
                command = "cd /d " + fullPath + " && echo ";

                for (int i = 0; i < Editing.getText().length(); i++) {
                    if (Editing.getText().charAt(i) == '\n') {
                        command += " >> " + which_file;
                    }
                    command += Editing.getText().charAt(i);
                    if (Editing.getText().charAt(i) == '\n' && i + 1 != Editing.getText().length()) {
                        command += "echo ";
                    }
                }

                // System.out.println(FilePathSave.getText());
                // if (FilePathSave.getText().equals("")) {
                // System.out.println("asdf");
                // }

                // for (int i = 0; i < Editing.getText().length(); i++) {
                // System.out.print(Editing.getText().charAt(i));
                // }
                // System.out.println(Editing.getText());
                System.out.println(command);

                ProcessBuilder t = new ProcessBuilder("cmd", "/c", command);
                Process oProcess = t.start();
                BufferedReader stdOut = new BufferedReader(new InputStreamReader(oProcess.getInputStream()));
                BufferedReader stdError = new BufferedReader(new InputStreamReader(oProcess.getErrorStream()));

                // String input = "";
                // boolean is_it_okay = false;

                // while ((s = stdOut.readLine()) != null) {
                // is_it_okay = true;
                // input += s + "\n";
                // }
                // while ((s = stdError.readLine()) != null) {
                // input += s + "\n";
                // }

                // if (is_it_okay) {
                // // 파일 존재할 경우 - Editing 화면에 출력
                // Editing.setText(input);
                // } else {
                // // 존재하지 않는 파일일 경우 - result화면에 오류 메세지 출력
                // Result.setText(input);
                // }
                // Editing.getText();
            } catch (IOException e) {
                // TODO : handle exception
                Result.setText("에러! 파일 실행 실패\n" + e.getMessage());
            }
        }

        // 자바 파일을 컴파일하는 함수
        public void File_Compile() {
            try {
                // cd하고 && javac사이에 있는 건 파일 경로

                // cmd가 자바 파일을 컴파일하도록 명령
                // 컴파일하게 되면 filename의 class파일이 만들어짐
                command = "cd /d " + fullPath + " && javac " + Filename;

                ProcessBuilder t = new ProcessBuilder("cmd", "/c", command);
                Process oProcess = t.start();
                BufferedReader stdOut = new BufferedReader(new InputStreamReader(oProcess.getInputStream()));
                BufferedReader stdError = new BufferedReader(new InputStreamReader(oProcess.getErrorStream()));

                while ((s = stdOut.readLine()) != null) {
                    isCompileError = true;
                }
                while ((s = stdError.readLine()) != null) {
                    isCompileError = true;
                }

                if (!isCompileError) {
                    // 컴파일 성공
                    Result.setText("compiled successfully");
                } else {
                    // 컴파일 실패
                    Result.setText("comile error occurred -" + Filename + ".error");
                }
            } catch (IOException e) {
                // TODO : handle exception
                Result.setText("에러! 파일 실행 실패\n" + e.getMessage());
            }
        }

        // 자바 파일을 실행하는 함수
        public void File_run() {
            try {
                // cd하고 && java사이에 있는 건 파일 경로

                // cmd가 자바 파일을 실행하도록 명령
                command = "cd /d" + fullPath + " && java " + Filename;

                ProcessBuilder t = new ProcessBuilder("cmd", "/c", command);
                Process process = t.start();
                BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                String input = "";

                while ((s = stdOut.readLine()) != null) {
                    input += s + "\n";
                }
                while ((s = stdError.readLine()) != null) {
                    input += s + "\n";
                }
                // cmd의 종료값을 이 프로그램의 종료값으로
                input += "Exit Value : " + process.exitValue();

                Result.setText(input);
            } catch (Exception e) {
                // TODO: handle exception
                Result.setText("에러! 파일 실행 실패\n" + e.getMessage());
            }
        }

        public void Error_File() {
            try {
                String Errorfile = Filename + ".error";
                if (isCompileError) {
                    System.out.println(Errorfile + "\n");

                    try {
                        command = "cd /d " + fullPath + " && javac " + Filename;
                        ProcessBuilder t = new ProcessBuilder("cmd", "/c", command);
                        Process oProcess = t.start();

                        BufferedReader stdOut = new BufferedReader(new InputStreamReader(oProcess.getInputStream()));
                        BufferedReader stdError = new BufferedReader(new InputStreamReader(oProcess.getErrorStream()));

                        while ((s = stdOut.readLine()) != null) {
                            System.out.println(s);
                        }
                        while ((s = stdError.readLine()) != null) {
                            System.out.println(s);
                        }
                    } catch (IOException e) {
                        // TODO: handle exception
                        Result.setText("에러! 외부 명령어 실행에 실패.\n" + e.getMessage());
                    }
                } else {
                    Result.setText("오류 파일이 존재하지 않습니다.");
                }
            } catch (Exception e) {
                // TODO: handle exception
                Result.setText("에러! 오류 파일 읽기 실패: " + e.getMessage());
            }
        }
    }

    //
    class file_action implements ActionListener {

        // 버튼 종류에 따라 1~6의 과정을 수행하도록 구현
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton b = (JButton) e.getSource();

            if (b.getText().equals("Open")) {
                be.file_open_action();
            } else if (b.getText().equals("Save")) {
                be.file_save_aciton();
            } else if (b.getText().equals("Compile")) {
                be.file_compile_action();
            } else if (b.getText().equals("Run")) {
                be.file_run_action();
            } else if (b.getText().equals("Delete")) {
                be.file_delete_action();
            } else if (b.getText().equals("Save Error")) {
                be.file_error_action();
            } else {
                be.file_clear_action();
            }
        }
    }

    // 백앤드
    class Backend {
        // 오픈 버튼을 눌렀을때?
        public void file_open_action() {
            // 입력한 파일 경로
            String Path = FilePath.getText();
            // 이 경로를 베이스로 마지막 \의 인덱스를 저장 (저장된 인덱스로부터 바로 다음 인덱스부터는 파일의 이름과 확장자)
            // 이러면 O(N)의 시간복잡도가 나오긴 하는데 ide.fullPath의 길이가 10^9이상만 아니면 되니까 무난할듯
            int current_index = 0;

            // result, editing창 초기화
            Result.setText("");
            Editing.setText("");

            for (int i = 0; i < Path.length(); i++) {
                if (Path.charAt(i) == '\\') {
                    current_index = i;
                }
            }
            for (int i = 0; i < current_index + 1; i++) {
                ide.fullPath += Path.charAt(i);
            }
            for (int i = current_index + 1; i < Path.length(); i++) {
                ide.Filename += Path.charAt(i);
            }
            // System.out.println(ide.fullPath);
            // System.out.println(ide.Filename);
            ide.File_print();
        }

        public void file_save_aciton() {
            // 구현중..
            // String ssss = Editing.getText();
            // System.out.println(ssss);
            ide.File_save();
        }

        public void file_compile_action() {
            if (ide.Filename.equals("")) {
                Result.setText("파일이 업로드 되지 않음.");
            } else {
                ide.File_Compile();
            }
        }

        public void file_run_action() {
            if (ide.Filename.equals("")) {
                Result.setText("파일이 업로드 되지 않음.");
            } else if (ide.isCompileError) {
                Result.setText("컴파일 에러 - 실행 불가");
            } else {
                ide.File_run();
            }
        }

        public void file_error_action() {
            String error_file = ide.Filename + ".error";
            if (ide.isCompileError) {
                Result.setText(error_file + '\n');
            } else {
                Result.setText("오류 파일이 존재하지 않습니다");
            }
        }

        public void file_delete_action() {
            if (ide.Filename.equals("")) {
                Result.setText("오류, 업로드된 파일이 없음");
            } else {
                ide.Filename = "";
                ide.isCompileError = false;
                Result.setText("파일 삭제 완료");
            }
        }

        public void file_clear_action() {
            FilePath.setText("");
            FilePathSave.setText("");
            Editing.setText("");
            Result.setText("");
            ide.fullPath = "";
            ide.Filename = "";
            ide.isCompileError = false;
        }
    }

    public static void main(String[] args) {
        new IDE_2();
    }
}
