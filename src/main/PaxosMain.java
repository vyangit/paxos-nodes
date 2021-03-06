package main;

import java.io.IOException;
import java.util.Scanner;

import commands.CommandManager;
import nodes.DistributedNode;

public class PaxosMain {
	
	/**
	 * @param args[0] Entry node remote ip address
	 */
	
	public static void main(String[] args) {
		// Acquiring local node addr

		System.out.println("-- Starting up seeding node --");
		DistributedNode paxosNode = null;
		try {
			paxosNode = new DistributedNode();
			System.out.println("-- Node created --");
			System.out.printf("Server address: %s\n", paxosNode.serverIp);
			System.out.printf("Server port (Use this to connect to server): %d\n", paxosNode.getServerSocket().getLocalPort());
			System.out.printf("Paxos port: %d\n", paxosNode.getPaxosSocket().getLocalPort());

		} catch (IOException e) {
			System.out.println("-- Seeding failed: Shutting down paxos node --");
			System.exit(1);
		}
		
		if (args.length == 0) {
			// Initial seeder node does nothing
		} else if (args.length == 1){
			// Initialize connecting node
			String remoteAddress = args[0];
			
			System.out.println("-- Establishing network connection to "+ remoteAddress +" --");
			if (paxosNode.joinNetwork(remoteAddress)) {
				System.out.println("-- Connection with network established --");
			} else {
				System.out.println("-- Connection with "+remoteAddress+" failed --");
				System.out.println("-- Shutting down paxos node --");
				System.exit(1);
			}			
		}
		
		// Common paxos node logic
		Scanner cmdStream = new Scanner(System.in);
		boolean shutdownFlag = false;
		
		while (!shutdownFlag) {
			System.out.println("Input a command, 'help' for valid commands, or 'exit' to shutdown the node: ");
			System.out.printf("$ ");
			shutdownFlag = CommandManager.processCommand(cmdStream.nextLine().trim());
		}
		
		System.out.println("-- Shutting down command stream --");
		cmdStream.close();
		System.out.println("-- Shutting down paxos node --");
		System.exit(0);
	}
}
