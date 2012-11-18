package websiteschema.mpsegment.core;

import websiteschema.mpsegment.conf.MPSegmentConfiguration;
import websiteschema.mpsegment.hmm.NodeRepository;
import websiteschema.mpsegment.hmm.Pi;
import websiteschema.mpsegment.hmm.Transition;
import websiteschema.mpsegment.util.FileUtil;
import websiteschema.mpsegment.util.SerializeHandler;

import java.io.DataInputStream;
import java.io.IOException;

public class POSResources {

    private static POSResources instance = new POSResources();

    private int[] posFreq;
    private Transition transition;
    private Pi pi;
    private NodeRepository stateBank;

    public static POSResources getInstance() {
        return instance;
    }

    private POSResources() {
        initialize();
    }

    private void initialize() {
        transition = new Transition();
        pi = new Pi();
        stateBank = new NodeRepository();

        String resource = MPSegmentConfiguration.getInstance().getPOSMatrix();
        try {
            SerializeHandler readHandler = new SerializeHandler(
                    new DataInputStream(FileUtil.getResourceAsStream(resource)));
            load(readHandler);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void load(SerializeHandler readHandler) throws IOException {
        posFreq = readHandler.deserializeArrayInt();
        transition.load(readHandler);
        pi.load(readHandler);
        stateBank.load(readHandler);
    }

    public int[] getPosFreq() {
        return posFreq;
    }

    public Transition getTransition() {
        return transition;
    }

    public Pi getPi() {
        return pi;
    }

    public NodeRepository getStateBank() {
        return stateBank;
    }
}
