/**
 * Author: Naifan Gao
 * Time: Sep 23, 2018
 * 
 * Thought: 
 * As the file may be huge, it's impossible to read all its data into memory
 * So I use Stream to process on each line, and store them as a list
 **/

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.io.*;
import java.lang.*;

public class FireWall {
	private String filePath;
	private FileInputStream inputStream;
	private List<Rule> rules;
	
	public class Rule {
		private String direction;
		private String protocol;
		private String port;
		private String ip;
		
		public Rule(String[] rule) {
			this.direction = rule[0];
			this.protocol = rule[1];
			this.port = rule[2];
			this.ip = rule[3];
		}
	}
	private Function<String, Rule> mapToItem = (line) ->{
		String[] l = line.split(",");
		Rule r = new Rule(l);
		return r;
	};
	
	public FireWall(String file) {
		/* Please pass the full path of the file, 
		or put file with current directory*/
		this.filePath = file;
		try {
			File f = new File(file);
			inputStream = new FileInputStream(f);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			rules = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean accept_packet(String direction, String protocol, int port, String ip_address) {
		if(!validDirection(direction) || !validProtocol(protocol) || !validPort(port) || !validIP(ip_address)) return false;
		for(Rule r:rules) {
			// find the rule we need by first two elements
			if(!direction.equals(r.direction) || !protocol.equals(r.protocol)) continue;
			
			// then try on the rest two
			if(checkProtocol(r.port, port) && checkIP(r.ip, ip_address)) return true;
		}
		return false;
	}
	
	public boolean validDirection(String direction) {
		return direction.equals("inbound") || direction.equals("outbound");
	}
	
	public boolean validProtocol(String prot) {
		return prot.equals("tcp") || prot.equals("udp");
	}
	
	public boolean validPort(int port) {
		return port>=1 && port<=65535;
	}
	
	public boolean validIP(String ip) {
		if(ip==null || ip.isEmpty() || ip.charAt(0)=='.' || ip.charAt(ip.length()-1)=='.') return false;
		String[] g = ip.split("\\.");
		if(g.length!=4) return false;
		for(String i:g) {
			int num = Integer.parseInt(i);
			if(num<0 || num>255) return false;
		}
		return true;
	}
	
	public boolean checkProtocol(String protocol, int target) {
		if(protocol.contains("-")) {
			String[] range = protocol.split("-");
			String t= Integer.toString(target);
			return t.compareTo(range[0])>=0 && t.compareTo(range[1])<=0;
		}else {
			int port = Integer.valueOf(protocol);
			return port==target;
		}
	}
	
	public boolean checkIP(String ips, String target) {
		if(ips.contains("-")) {
			String[] range = ips.split("-");
			return target.compareTo(range[0])>=0 && target.compareTo(range[1])<=0;
		}else {
			return ips.equals(target);
		}
	}
	
	public static void main(String[] args) {
		FireWall wall = new FireWall("test.csv");
		System.out.println(wall.accept_packet("inbound", "tcp", 80, "192.168.1.2"));
		System.out.println(wall.accept_packet("inbound", "udp", 53, "192.168.2.1"));
		System.out.println(wall.accept_packet("outbound", "tcp", 10234, "192.168.10.11"));
		System.out.println(wall.accept_packet("inbound", "tcp", 81, "192.168.1.2"));
		System.out.println(wall.accept_packet("inbound", "udp", 24, "52.12.48.92"));
	}
	
}
