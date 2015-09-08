package sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MemoryMonitor {
	public static String[] listRunningProcesses(String processName) {
		String[] processes = new String[2];
		try {
			String line;
			StringTokenizer temp;
			Process p = Runtime.getRuntime().exec("tasklist.exe /FI \"IMAGENAME eq \""+processName+" /FO CSV /NH");
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				if (!line.trim().equals("")) {
					// keep only the proecess name
					line = line.replace("\",\"", "^").replace("\"", "").replace(",", "");
					temp = new StringTokenizer(line, "^");
					
					int i = 0;
					int v = 0;
					while (temp.hasMoreTokens()) {
						if(i == 1 || i == 4) {
							processes[v] = temp.nextToken();
							v++;
						} else temp.nextToken();
						i++;
					}
				}
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return processes;
	}
	
	public static void main(String[] args) {
		String[] processes = listRunningProcesses("TeamViewer_Service.exe");
		
		System.out.println("pid: "+processes[0]+", mem: "+processes[1]);
	}
	
}