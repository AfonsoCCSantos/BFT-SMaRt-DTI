package intol.bftmap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bftsmart.tom.MessageContext;
import bftsmart.tom.server.defaultservices.DefaultSingleRecoverable;
import intol.bftmap.models.Coin;
import intol.bftmap.models.NFT;

public class DTIServer extends DefaultSingleRecoverable {
	
	private final Logger logger = LoggerFactory.getLogger("bftsmart");
	private TreeMap<Integer, Coin> coinsMap;
	private TreeMap<Integer, NFT> nftsMap;
	
	@Override
	public byte[] appExecuteOrdered(byte[] command, MessageContext msgCtx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] appExecuteUnordered(byte[] command, MessageContext msgCtx) {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public byte[] getSnapshot() {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutput out = new ObjectOutputStream(bos)) {
            //out.writeObject(replicaMap);
            out.flush();
            bos.flush();
            return bos.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace(); //debug instruction
            return new byte[0];
        }
	}
	
	@Override
	public void installSnapshot(byte[] state) {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(state); ObjectInput in = new ObjectInputStream(bis)) {
			//replicaMap = (TreeMap<K, V>) in.readObject();
        } catch (Exception ex) {
            ex.printStackTrace(); //debug instruction
        }
	}
	
}
