import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class ExcelDemo extends JFrame{
	
	private JScrollPane scrollPane; //스크롤패널
	private JTable table, headerTable; //테이블, 헤더테이블
	private JMenuBar menuBar; //메뉴바
	private JMenu fileMenu, formulasMenu, functionMenu; //파일메뉴, 공식메뉴, 함수메뉴
	private JMenuItem newItem, open, save, exit, sum, average, count, max, min; //각종 기능 메뉴
	private String title; //타이틀
	private int cardinality, degree; //선택한  행, 열
	private enum functionName{SUM,AVERAGE,COUNT,MAX,MIN}; //함수이름 enum
	
	ExcelDemo(){
		//테이블 생성, 프레임  화면 출력, 이벤트 처리
		title="새 Microsoft Excel 워크시트.xlsx - Excel"; //기본 프레임 제목
		cardinality = degree = 0; //선택한 행
		
		setTitle(title); //제목 설정
		setSize(610,588); //크기 설정
		setLocationRelativeTo(null); //화면 중앙 출력을 위한 상대 위치 처리
		setDefaultCloseOperation(EXIT_ON_CLOSE); //닫기 버튼 클릭 시 메모리 소멸하도록 설정
		
		
		table = new JTable(100,26); //table 생성 (100 row * 26 column)
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); //크기 자동변경 불가능 설정
		table.getTableHeader().setResizingAllowed(false); //크기 변경 불가능 설정
		table.getTableHeader().setReorderingAllowed(false); //셀 이동 불가능 설정
		table.setCellSelectionEnabled(true); //셀 선택시 드래그 영역만 색칠되게 설정
		table.addMouseListener(new TableListener()); //table 이벤트 add
	
        headerTable = new JTable(100,1); //헤더 테이블 생성
		for(int i=0;i<table.getRowCount();i++) headerTable.setValueAt(Integer.toString(i),i,0); //헤더테이블 숫자 라벨링		
		headerTable.setShowGrid(false); //헤더테이블의 격자 제거
        headerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); //크기 자동변경 불가능 설정
        headerTable.setPreferredScrollableViewportSize(new Dimension(40, 0)); //헤더 테이블 창 폭 설정
        headerTable.getColumnModel().getColumn(0).setPreferredWidth(40); //헤더 테이블 셀 폭 설정
        
        //headerTable TableCellRenderer 교체
        //TableCellRenderer는 table의 header쪽 Renderer쓰도록 설정
        //table에서 열의 선택여부를 가져와서 색상 변경을 할지 선택
        //선택하면 빨간 볼드체로 변경
        //셀 내용 중앙 정렬
        headerTable.getColumnModel().getColumn(0).setCellRenderer(new TableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable x, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            	boolean selected = table.isRowSelected(row); //셀 선택 여부
            	//table header renderer 가져오기
                Component component = table.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(table, value, false, false, -1, -2);
                ((JLabel) component).setHorizontalAlignment(SwingConstants.CENTER); //중앙 정렬
                if (selected) {
                	//폰트 변경
                    component.setFont(component.getFont().deriveFont(Font.BOLD));
                    component.setForeground(Color.red);
                } else {
                    component.setFont(component.getFont().deriveFont(Font.PLAIN));
                }
                return component;
            }
        });
        headerTable.setEnabled(false); //헤더 테이블 수정 못하게 설정
        //즉각적으로 선택한 열의 색상이 변경되기 위해서 UI를 업데이트하는 이벤트 add
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                headerTable.updateUI();
            }
        });
        //table가 있는 스크롤패널 생성(스크롤바 2개 있도록 설정)
		scrollPane = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setRowHeaderView(headerTable); //RowHeaderView에 헤더테이블 넣기
		
		createMenu(); //createMenu 메소드 호출
		
		add(scrollPane); //스크롤패인 프레임에 넣기
		
		setVisible(true); //화면 출력
		
		
	}
	private void createMenu() {
		//메뉴 생성 
		//각종 메뉴를 만들어주고 이벤트와 연결시키는 메소드
		
		menuBar = new JMenuBar(); //메뉴바 생성
		
		//메뉴와 메뉴아이템 생성
		fileMenu = new JMenu("File");
		formulasMenu = new JMenu("Formulas");
		functionMenu = new JMenu("Function");
		newItem = new JMenuItem("New");
		open = new JMenuItem("Open");
		save = new JMenuItem("Save");
		exit= new JMenuItem("Exit");
		sum = new JMenuItem("SUM");
		average = new JMenuItem("AVERAGE");
		count = new JMenuItem("COUNT");
		max = new JMenuItem("MAX");
		min = new JMenuItem("MIN");
		
		//newItem 이벤트연결
		newItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new ExcelDemo(); //새 창 만들기
				dispose(); //현재 창 종료
			}
			
		});
		FileListener fil = new FileListener(); //file관련 이벤트
		//open, save 이벤트 연결
		open.addActionListener(fil);
		save.addActionListener(fil);
		
		//종료 이벤트 연결
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0); //종료
			}
			
		});
		
		FunctionListener ful = new FunctionListener(); //함수관련 이벤트
		//sum, average, count, max, min 메뉴 이벤트 연결
		sum.addActionListener(ful);
		average.addActionListener(ful);
		count.addActionListener(ful);
		max.addActionListener(ful);
		min.addActionListener(ful);
		
		//메뉴아이템과 메뉴와 메뉴바 연결
		functionMenu.add(sum);
		functionMenu.add(average);
		functionMenu.add(count);
		functionMenu.add(max);
		functionMenu.add(min);
		
		formulasMenu.add(functionMenu);
		
		fileMenu.add(newItem);
		fileMenu.add(open);
		fileMenu.addSeparator();
		fileMenu.add(save);
		fileMenu.addSeparator();
		fileMenu.add(exit);
		
		menuBar.add(fileMenu);
		menuBar.add(formulasMenu);
		
		setJMenuBar(menuBar); //메뉴바 설정
		return;
	}
	
	//table 이벤트
	class TableListener extends MouseAdapter{
		public void mousePressed(MouseEvent e) {
			//마우스가 눌리면 변수에 table에서 선택한 행과 열 정보 저장
			cardinality = table.getSelectedRow();
			degree = table.getSelectedColumn();
			return;
		}
	}
	
	//파일 관련 이벤트
	class FileListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser(); //JFileChooser
			//파일필터 객체 생성
			javax.swing.filechooser.FileFilter ff = new javax.swing.filechooser.FileFilter() {

				@Override
				public boolean accept(File file) {
					//파일 확장자가 txt나 csv인 경우만 허용
					String name = file.getName();
					return name.endsWith(".csv") || name.endsWith(".txt");
				}

				@Override
				public String getDescription() {
					//디스크립션 설정
					return "txt 또는 csv 파일";
				}
				
			};
			chooser.setFileFilter(ff); //파일 필터 설정 
			
			if(e.getActionCommand()=="Open") {
				//Open 명령어
				//Dialog에서 파일을 제대로 선택한 경우
				if(chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
					
					File sf = chooser.getSelectedFile(); //선택한 파일 가져오기
					
					BufferedReader br = null; //BufferedReader
					StringTokenizer st = null; //StringTokenizer
					
					try {
						br = new BufferedReader(new FileReader(sf)); //가져온 파일 읽어오기
						String line; //line 저장 스트링
						int i=0,j=0; //셀 위치 변수
						while((line=br.readLine()) != null) { //모두 불러오기
							st = new StringTokenizer(line,","); //line을 tokenize
							j=0; //셀 열 초기화
							while(st.hasMoreTokens()) {
								String tmp = st.nextToken(); //tmp에 다음 토큰 저장
								table.setValueAt(tmp.substring(0,tmp.length()-1), i, j); //table에 값 설정
								j++;
							}
							i++;
						}
						
						br.close(); //BufferedReader 닫기
						title=sf.getAbsolutePath(); //제목 변경
						setTitle(title); //제목 갱신
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
				}
			}
			else {
				//Save 명령어
				//Dialog에서 파일을 제대로 선택한 경우
				if(chooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION) {
					File sf = chooser.getSelectedFile(); //선택한 파일 가져오기
					
					title=sf.getAbsolutePath(); //제목 변경
					setTitle(title); //제목 설정
					
					BufferedWriter bw = null; //BufferedWriter
					
					try {
						
						bw = new BufferedWriter(new FileWriter(sf)); //선택한 파일 읽어오기
						for(int i=0;i<table.getRowCount();i++) {
							String line="";//line 저장 스트링
							for(int j=0;j<table.getColumnCount();j++) {
								String tmp = (String) table.getValueAt(i,j); //table 값 불러오기
								line+=(tmp==null?"":tmp)+" ,"; //line에 붙이기
							}
							line = line.substring(0,line.length()-1)+"\r\n"; //맨 뒤 구분자 삭제 후 개행 넣기
							bw.write(line); //line 입력 후 개행
						}
						bw.close(); //bufferedwriter 닫기
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
				}
			}
		}
		
	}
	
	//함수 이벤트
	class FunctionListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//Input 다이얼로그로 입력 받은 문자열 저장
			String input = JOptionPane.showInputDialog(null, "Function Arguments", e.getActionCommand(), JOptionPane.PLAIN_MESSAGE);
			//좌측상단, 우측하단 문자열
			String topLeft, bottomRight;
			int rowLeft, rowRight, colLeft, colRight; //좌측행, 우측행, 좌측열, 우측열 좌표
			try {
				StringTokenizer st = new StringTokenizer(input, ":"); //StringTokenizer (콜론 구분자)
				
				topLeft = st.nextToken(); //좌측상단 문자열
				bottomRight = st.nextToken(); //우측하단 문자열
				rowLeft = Integer.parseInt(topLeft.substring(1)); //앞 알파벳을 제외한 숫자를 파싱
				rowRight = Integer.parseInt(bottomRight.substring(1)); //앞 알파벳을 제외한 숫자를 파싱
				colLeft = topLeft.charAt(0)-'A'; //앞 알파벳으로 위치 산출 (A는 0으로 설정)
				colRight = bottomRight.charAt(0)-'A'; //앞 알파벳으로 위치 산출
			}catch(Exception _e) {
				return; //의도와 벗어난 값이 입력되면 이벤트 무시
			}
			
			
			double sum = 0, min = 0, max = 0; //합, 최소, 최대 저장 변수
			int count = 0; //개수
			try {
				//모든 셀 범위 순회
				for(int i=rowLeft;i<=rowRight;i++) {
					for(int j=colLeft;j<=colRight;j++) {
						Object x = table.getValueAt(i,j); //그 위치 셀 가져오기
						try {
							double val = Double.parseDouble((String) x); //실수형으로 파싱 시도
							sum+=val; //sum에 입력값 더하기
							count++; //개수 증가
							if(count == 1) {
								min = max = val; //만약 처음 값이 들어왔으면  min, max 처음 값으로 초기화
							}
							else {
								//min, max 갱신
								if(min>val) min = val;
								if(max<val) max = val;
							}
						} catch(Exception _e) {
							continue; //숫자가 아니면 넘기기
						}
					}
				}
			}catch(Exception _e) {
				return; //셀 범위를 넘었으면 이벤트 무시
			}
			
			//함수이름 enum에 맞게 값 list에 넣기 (sum, average, count, max, min)
			String[] list = {Double.toString(sum),Double.toString(count==0?0:sum/count),Integer.toString(count),Double.toString(max),Double.toString(min)};
			
			//개수가 0개이면 count 및 sum 함수를 제외한 함수들의 값이 정의되지 않는다.
			//결과값을 정의할 수 있는 경우만 table에 표시
			if(count!=0 || e.getActionCommand() == "SUM" || e.getActionCommand() == "COUNT")
					table.setValueAt(list[functionName.valueOf(e.getActionCommand()).ordinal()], cardinality, degree);
			return;
			
		}
	}
	
	//메인 메소드
	public static void main(String[] args) {
		new ExcelDemo();
	}
}