package intol.bftmap;

import intol.bftmap.models.Coin;
import intol.bftmap.models.NFT;
import java.io.Console;

public class DTIClient {
	
	public static void main(String[] args) {
		int clientId = (args.length > 0) ? Integer.parseInt(args[0]) : 1001;
		BFTMap<Integer, Coin> coinMap = new BFTMap<>(clientId);
		BFTMap<Integer, NFT> nftMap = new BFTMap<>(clientId);
		
		Console console = System.console();
		
        System.out.println("\nCommands:\n");
        System.out.println("\tMY_COINS(): get ids and values of coins associated with current user");
        System.out.println("\tMINT(value): create coin for current user (returns coin id)");
        System.out.println("\tSPEND(coins, receiver, value): send coin to other user");
        System.out.println("\tMY_NFTS(): list user owned nfts");
        System.out.println("\tMINT_NFT(name, uri, value): create NFT for current user (returns id)");
        System.out.println("\tSET_NFT_PRICE(nft, value): change price of nft (requires value)");
        System.out.println("\tSEARCH_NFT(text): lists id, name, uri, and value of NFTs whose name contains the provided text");
        System.out.println("\tBUY_NFT(nft, coins): buys NFT(id) if user has enough coins");
        System.out.println("\tEXIT: Terminate this client\n");
		
		while (true) {
            String cmd = console.readLine("\n  > ");

            if (cmd.equalsIgnoreCase("MY_COINS")) {

            } 
            else if (cmd.equalsIgnoreCase("MINT")) {
				
				double value;
                try {
                    value = Integer.parseInt(console.readLine("Enter a value: "));
                } catch (NumberFormatException e) {
                    System.out.println("\tThe value is supposed to be a number!\n");
                    continue;
                }
              
                //invokes the op on the servers
                int id = coinMap.mintCoin(value);
                if (id == -1) {
                	System.out.println("\n The operation failed");
                }
                else {
                	System.out.println("\nThe coin with the value given was added to the map with id " + id + ".\n");		
                }
            } else if (cmd.equalsIgnoreCase("SPEND")) {

            } else if (cmd.equalsIgnoreCase("MY_NFTS")) {

            } else if (cmd.equalsIgnoreCase("MINT_NFT")) {
				String name = console.readLine("Enter an alpha-numeric name for the NFT: ");
                
                String uri= console.readLine("Enter the NFT uri: ");
                double value;
                try {
                    value = Integer.parseInt(console.readLine("Enter a the value of the NFT: "));
                } catch (NumberFormatException e) {
                    System.out.println("\tThe value is supposed to be a number!\n");
                    continue;
                }
                
                //invokes the op on the servers
                int id = nftMap.mintNFT(name, uri, value);
                
                if(id == -1) {
					System.out.println("\n The operation failed.\n");
				} else {
 	               System.out.println("\nThe NFT was added to the map with id " + id + ".\n");	
				}
                
                
			} else if (cmd.equalsIgnoreCase("SET_NFT_PRICE")) {


			} else if (cmd.equalsIgnoreCase("SEARCH_NFT")) {


			} else if (cmd.equalsIgnoreCase("BUY_NFT")) {



            } else if (cmd.equalsIgnoreCase("EXIT")) {

            } else {
                System.out.println("\tInvalid command :P\n");
            }
        }
		
		
		
		
		
		
		
		
	}

}
