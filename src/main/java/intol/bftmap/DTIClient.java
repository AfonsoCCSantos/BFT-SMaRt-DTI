package intol.bftmap;

import intol.bftmap.models.Coin;
import intol.bftmap.models.NFT;
import java.io.Console;
import java.util.List;
import java.util.ArrayList;



public class DTIClient {
	
	public static void main(String[] args) {
		int clientId = (args.length > 0) ? Integer.parseInt(args[0]) : 1001;
		DTIStub map = new DTIStub(clientId);
		
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
                //invokes the op on the servers
                List<Coin>  myCoins = map.getMyCoins();
                if (myCoins == null) {
                	System.out.println("\n The operation failed");
                } 
                else {
					if (myCoins.isEmpty()) {
						System.out.println("\nYou dont have any coins.\n");
					}
					else {
						System.out.println("\nYour coins:\n");
                    	for(Coin c : myCoins) {
                        	System.out.println("Coin " + c.getId() + " with value " + c.getValue() + "\n");
                    	} 	
					}
                     
                }

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
                int id = map.mintCoin(value);
                if (id != -1) {
                	System.out.println("\nThe coin with the value given was added to the map with id " + id + ".\n");
                }
            } else if (cmd.equalsIgnoreCase("SPEND")) {
                List<Integer> coinIds = new ArrayList<>();
                int receiverId;
                double value;
                try {
                    while(true) {
                        int id = Integer.parseInt(console.readLine("Please insert the id of a coin or -1 to stop listing: "));
                        if(id == -1) break;   
                        coinIds.add(id);
                    } 
                    receiverId = Integer.parseInt(console.readLine("Enter the receiver id: "));
                } catch (NumberFormatException e) {
                    System.out.println("\tThe value is supposed to be an integer!\n");
                    continue;
                }

                try {
                    value = Double.parseDouble(console.readLine("Enter the value to spend: "));
                } catch (NumberFormatException e) {
                    System.out.println("\tThe value is supposed to be a number!\n");
                    continue;
                }

                //invokes the op on the servers
                int id = map.spendCoins(coinIds, receiverId, value);
                if (id == -1) {
                	System.out.println("\nThe operation failed");
                }
                else if(id == 0) {
                    System.out.println("\nNo change.");
                }
                else {
                	System.out.println("\nNew coin (change) created with id " + id + ".\n");
                }

            } else if (cmd.equalsIgnoreCase("MY_NFTS")) {

                //invokes the op on the servers
                List<NFT> myNFTs = map.getMyNFTs();

                if (myNFTs == null) {
                	System.out.println("\n The operation failed");
                } 
                else {
					if (myNFTs.isEmpty()) {
						System.out.println("\nYou dont have any NFTs.\n");
					}
					else {
						System.out.println("\nYour NFTs:\n");
                    for(NFT nft : myNFTs) {
                        System.out.println("NFT with id " + nft.getId() + " with name " + nft.getName() + 
                                ", with uri " + nft.getUri() + " and value " + nft.getValue() + " \n");
                    } 
					}
                }

            } else if (cmd.equalsIgnoreCase("MINT_NFT")) {
				String name = console.readLine("Enter an alpha-numeric name for the NFT: ");
                
                String uri= console.readLine("Enter the NFT uri: ");
                double value;
                try {
                    value = Double.parseDouble(console.readLine("Enter a the value of the NFT: "));
                } catch (NumberFormatException e) {
                    System.out.println("\tThe value is supposed to be a number!\n");
                    continue;
                }
                
                //invokes the op on the servers
                int id = map.mintNFT(name, uri, value);
                
                if(id == -1) {
					System.out.println("\n The operation failed.\n");
				} else {
 	               System.out.println("\nThe NFT was added to the map with id " + id + ".\n");	
				}
                
                
			} else if (cmd.equalsIgnoreCase("SET_NFT_PRICE")) {
                int nftId;
                double newValue;
                try {
                    nftId = Integer.parseInt(console.readLine("Enter the id of the NFT to be changed: "));
                } catch (NumberFormatException e) {
                    System.out.println("\tThe value is supposed to be an integer!\n");
                    continue;
                }

                try {
                    newValue = Double.parseDouble(console.readLine("Enter the new value for the NFT: "));
                } catch (NumberFormatException e) {
                    System.out.println("\tThe value is supposed to be a number!\n");
                    continue;
                }
                
                // invokes the op on the servers
                int id = map.setNFTPrice(nftId, newValue);
                if (id == -1) {
                	System.out.println("\n The operation failed");
                }
                else {
                	System.out.println("\nThe NFT with id " + id + " was changed.\n");		
                }
			} else if (cmd.equalsIgnoreCase("SEARCH_NFT")) {
                String text = console.readLine("Enter an alpha-numeric name to be searched: ");

                //invokes the op on the servers
                List<NFT> resultNFTs = map.searchNFT(text);

                if (resultNFTs == null) {
                	System.out.println("\n The operation failed");
                } 
                else {
                    System.out.println("\nResult NFTs:\n");
                    for(NFT nft : resultNFTs) {
                        System.out.println("NFT with id " + nft.getId() + " with name " + nft.getName() + 
                                ", with uri " + nft.getUri() + " and value " + nft.getValue() + " \n");
                    }  
                }
			} else if (cmd.equalsIgnoreCase("BUY_NFT")) {
                List<Integer> coinIds = new ArrayList<>();
                int nftId;
                try {
                    while (true) {
                        int id = Integer.parseInt(console.readLine("Please insert the id of a coin or -1 to stop listing: "));
                        if (id == -1) break;   
                        coinIds.add(id);
                    } 
                    nftId = Integer.parseInt(console.readLine("Enter the nft id: "));
                } catch (NumberFormatException e) {
                    System.out.println("\tThe value is supposed to be an integer!\n");
                    continue;
                }

                //invokes the op on the servers
                int id = map.buyNFT(coinIds, nftId);
                if (id == -1) {
                	System.out.println("\nThe operation failed");
                }
                else if (id == 0) {
                    System.out.println("\nNo change.");
                }
                else {
                	System.out.println("\nNew coin (change) created with id " + id + ".\n");
                }

            } else if (cmd.equalsIgnoreCase("EXIT")) {
                System.out.println("\tShutting down...\n");
                System.exit(0);

            } else {
                System.out.println("\tInvalid command :P\n");
            }
        }
		
		
		
		
		
		
		
		
	}

}
