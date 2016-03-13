package xMiner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.parabot.core.Context;
import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.api.utils.Timer;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.events.MessageEvent;
import org.rev317.min.api.events.listeners.MessageListener;
import org.rev317.min.api.methods.Bank;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.methods.SceneObjects;
import org.rev317.min.api.methods.Skill;
import org.rev317.min.api.wrappers.SceneObject;
import org.rev317.min.api.wrappers.Tile;

@ScriptManifest(
author = "Xunn", 
category = Category.MINING, 
description = "...", 
name = "xMiner", 
servers = {"PKHonor"}, 
version = 0.002)

public class xMiner extends Script implements Paintable, MessageListener {
 
    public boolean guiWait = true;
    private final Color color1 = new Color(Color.green.hashCode());
   	private final Font font1 = new Font("Calibri", 0, 15);

    public Timer timer;
    public int startExperience;
	public String status = "null";
	public String resourceRock = "null";
	public int resourceCounter = 0;
	public String playerName = "Raymond Reddington";

	public final int[] coalRock = {2096,2097};
	public final int[][] resourceRocks={{2491},{90,91,94,95},{2092,2093},{2100,2101},{2096,2097},{2098,2099},{2102,2103},{2104,2105},{2106,2107},{20359}};
	public int[] resourceRockIDs;
	public final int[] resources = {7937,437,439,441,448,450,452,454};
	
	public static int depletedRock = 450;
	public final int depositBox = 9398;
	public final int closedChest = 3193;
	
	public final int[] bankObject = {depositBox,closedChest};
	
	private final ArrayList<Strategy> strategies = new ArrayList<Strategy>();
	public boolean onExecute() {
	timer = new Timer();
	startExperience = Skill.MINING.getExperience();
	//playerName = Players.getMyPlayer().getName().toString();
	strategies.add(new Miner());
	strategies.add(new Banking());
	provide(strategies);
	EventQueue.invokeLater(new Runnable() {
		public void run() {
			try {
				xMinerGui frame = new xMinerGui();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	});
	while(guiWait){
		sleep(300);
		//System.out.println("GUI is open");
		}
	return true;
	}
	
	
	@Override
	public void onFinish(){ 
		System.out.println("--------------------------------------");
		System.out.println("xMiner ran for: " + timer.toString());
		System.out.println("We mined "+resourceCounter+" ore's");
		System.out.println("Thank you for using xMiner");
		System.out.println("--------------------------------------");
	}
	
	@SuppressWarnings("serial")
	public class xMinerGui extends JFrame {

		private JPanel contentPane;

		public xMinerGui() {
			
			setTitle("xMiner GUI");
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setBounds(100, 100, 340, 195);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPane.setLayout(new BorderLayout(0, 0));
			setContentPane(contentPane);
			
			JPanel panelUI = new JPanel();
			panelUI.setBorder(new EmptyBorder(0, 0, 0, 0));
			contentPane.add(panelUI, BorderLayout.WEST);
			
			JLabel lblXminer = new JLabel("xMiner");
			lblXminer.setHorizontalAlignment(SwingConstants.CENTER);
			
			JComboBox comboBox = new JComboBox();
			comboBox.setToolTipText("Allows of Rock Selection");
			comboBox.setModel(new DefaultComboBoxModel(new String[] {"Pure Essence", "Copper/Tin", "Iron","Silver", "Coal","Gold", "Mithril", "Adamant", "Runite", "Marble"}));
			comboBox.setSelectedIndex(0);
			
			JButton btnStart = new JButton("Start");
			
			btnStart.setToolTipText("Press to Start!");
			
			JButton btnStop = new JButton("Stop");
			btnStop.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent m) {
					if(btnStop.getText().matches("Stop")){
						Context.getInstance().getRunningScript().setState(Script.STATE_STOPPED);
						dispose();
					}
				}
			});
		
			GroupLayout gl_panelUI = new GroupLayout(panelUI);
			gl_panelUI.setHorizontalGroup(
				gl_panelUI.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panelUI.createSequentialGroup()
						.addGroup(gl_panelUI.createParallelGroup(Alignment.LEADING)
							.addGroup(Alignment.TRAILING, gl_panelUI.createSequentialGroup()
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblXminer, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
							.addGroup(Alignment.TRAILING, gl_panelUI.createSequentialGroup()
								.addContainerGap()
								.addComponent(comboBox, 0, 99, Short.MAX_VALUE))
							.addGroup(Alignment.TRAILING, gl_panelUI.createSequentialGroup()
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnStart, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE))
							.addGroup(Alignment.TRAILING, gl_panelUI.createSequentialGroup()
								.addContainerGap()
								.addComponent(btnStop, GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)))
						.addContainerGap())
			);
			gl_panelUI.setVerticalGroup(
				gl_panelUI.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panelUI.createSequentialGroup()
						.addGap(24)
						.addComponent(lblXminer)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnStart)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnStop)
						.addContainerGap(15, Short.MAX_VALUE))
			);
			panelUI.setLayout(gl_panelUI);
					
			JPanel panelStatistics = new JPanel();
			contentPane.add(panelStatistics, BorderLayout.CENTER);
			panelStatistics.setLayout(new GridLayout(7, 2, 0, 0));
			
			JLabel lblTime = new JLabel("Time:");
			lblTime.setFont(new Font("Tahoma", Font.BOLD, 11));
			panelStatistics.add(lblTime);
			
			JLabel lblTimedata = new JLabel("null");
			panelStatistics.add(lblTimedata);
			
			JLabel lblStatus = new JLabel("Status:");
			lblStatus.setFont(new Font("Tahoma", Font.BOLD, 11));
			panelStatistics.add(lblStatus);
			
			JLabel lblStatusdata = new JLabel("null");
			panelStatistics.add(lblStatusdata);
			
			JLabel lblPlayersAround = new JLabel("Players Around:");
			lblPlayersAround.setFont(new Font("Tahoma", Font.BOLD, 11));
			panelStatistics.add(lblPlayersAround);
			
			JLabel lblPlayersArounddata = new JLabel("null");
			panelStatistics.add(lblPlayersArounddata);
			
			JLabel lblRock = new JLabel("Rock:");
			lblRock.setFont(new Font("Tahoma", Font.BOLD, 11));
			panelStatistics.add(lblRock);
			
			JLabel lblResourcedata = new JLabel("null");
			panelStatistics.add(lblResourcedata);
			
			JLabel lblRockdat = new JLabel("Resources:");
			lblRockdat.setFont(new Font("Tahoma", Font.BOLD, 11));
			panelStatistics.add(lblRockdat);
			
			JLabel lblResourceCount = new JLabel("null");
			panelStatistics.add(lblResourceCount);
			
			JLabel lblResourceh = new JLabel("Resource/h:");
			lblResourceh.setFont(new Font("Tahoma", Font.BOLD, 11));
			panelStatistics.add(lblResourceh);
			
			JLabel lblResourcehdata = new JLabel("null");
			panelStatistics.add(lblResourcehdata);
			
			JLabel lblExperienceh = new JLabel("Experience/h:");
			lblExperienceh.setFont(new Font("Tahoma", Font.BOLD, 11));
			panelStatistics.add(lblExperienceh);
			
			JLabel lblExperiencedata = new JLabel("null");
			panelStatistics.add(lblExperiencedata);
			
			ActionListener timerAction = new ActionListener()
		    {
		        public void actionPerformed(ActionEvent ae)
		        {
		        	lblTimedata.setText(timer.toString());
		        	lblStatusdata.setText(status);
		        	lblPlayersArounddata.setText(""+(Players.getNearest().length-1));
		        	lblResourcedata.setText(resourceRock);
		        	lblResourceCount.setText(""+resourceCounter);
		            lblResourcehdata.setText(""+timer.getPerHour(resourceCounter));
		            lblExperiencedata.setText(format(timer.getPerHour(Skill.MINING.getExperience()-startExperience)));
		        }
		    };
		    javax.swing.Timer guiTimer = new javax.swing.Timer(1000, timerAction);
		    
		    btnStart.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent m) {
					if(btnStart.getText().matches("Start")){
					guiTimer.start();
					resourceRockIDs = resourceRocks[comboBox.getSelectedIndex()];
					resourceRock = comboBox.getSelectedItem().toString();
					System.out.println("Mining: "+resourceRock);
					guiWait = false;
					btnStart.setText("Pause");
					btnStart.setToolTipText("Press to Pause!");
					} else if (btnStart.getText().matches("Pause")){
						Context.getInstance().getRunningScript().setState(Script.STATE_PAUSE);
						btnStart.setText("Resume");
						btnStart.setToolTipText("Press to Resume!");
						
					} else if (btnStart.getText().matches("Resume")){
						resourceRockIDs = resourceRocks[comboBox.getSelectedIndex()];
						resourceRock = comboBox.getSelectedItem().toString();
						System.out.println("Mining: "+resourceRock);
						Context.getInstance().getRunningScript().setState(Script.STATE_RUNNING);
						btnStart.setText("Pause");
						btnStart.setToolTipText("Press to Pause!");
					} 
				}
			});
		}
	}

	
	

	public class Miner implements Strategy {

		@Override
		public boolean activate() {
			SceneObject[] i = null;
			try{
            	i = SceneObjects.getNearest(resourceRockIDs);
            	}catch(NullPointerException e){}
			//TODO Players has a pickaxe
			return !Inventory.isFull() && 
					i != null &&
					i.length > 0 &&
					Players.getMyPlayer().getAnimation() == -1;
		}

		@Override
		public void execute() {
			status = "Mining";
			SceneObject[] i = null;
			try{
            	i = SceneObjects.getNearest(resourceRockIDs);
            	}catch(NullPointerException e){
            		System.out.println("SceneObjects.getNearest retured an error");
            	}
			i[0].interact(0);
			Tile currentRockTile = i[0].getLocation(); 
			Time.sleep(new SleepCondition(){
    			@Override
    			public boolean isValid(){
    				return resourceIsDepleted(currentRockTile) || Inventory.isFull() || Players.getMyPlayer().getAnimation() == 1353;
    			}
    			
    		},  5000);

		}

	}
	
	public class Banking implements Strategy {

	@Override
	public boolean activate() {
		SceneObject[] i = null;
		try{
        	i = SceneObjects.getNearest(bankObject);
        	}catch(NullPointerException e){}
		return Inventory.isFull() && 
				i != null &&
				i.length > 0;
	}

	@Override
	public void execute() {
		status = "Banking";
		if(!Bank.isOpen() && Inventory.isFull()){
		resourceCounter+=Inventory.getCount(resources);	
		status = "Opening Bank";
		SceneObject[] i = null;
		try{
        	i = SceneObjects.getNearest(bankObject);
        	}catch(NullPointerException e){
        		System.out.println("SceneObjects.getNearest retured an error");
        	}
		i[0].interact(0);
		Time.sleep(new SleepCondition(){
			@Override
			public boolean isValid(){
				return Bank.isOpen();
			}
			
		},  3000);
		} else if (Bank.isOpen() && Inventory.isFull()){
		status = "Depositing";
		Menu.sendAction(646, 1381, 5, 23412,1);	
		Time.sleep(new SleepCondition(){
			@Override
			public boolean isValid(){
				return Inventory.isEmpty();
			}
			
		},  2000);
	} if (Bank.isOpen() && Inventory.isEmpty()){
		status = "Closing Bank";
		Bank.close();
	}
}

}
	public boolean resourceIsDepleted(Tile t){
		for (SceneObject s: SceneObjects.getNearest(depletedRock)){
			if(s != null){
				if(s.getLocation().equals(t))
					return true;
			}
		}
		return false;
	}

	@Override
	public void messageReceived(MessageEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	private String format(int num){
		String POSTFIX = "";
		int NUM = num;
		if(num > 1000){
			NUM = num/1000;
			POSTFIX = "k";
		}
		return NUM + POSTFIX;
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(color1);
		g.setFont(font1);
		g.drawString("Status: "+status,10,15);
		g.drawString("State: "+Context.getInstance().getRunningScript().getState(), 10, 30);
	}
}
