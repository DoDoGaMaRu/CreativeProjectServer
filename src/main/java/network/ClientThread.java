package network;

import api.*;
import network.Exeptions.*;
import network.protocol.*;
import util.NameFormat;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Socket;

class ClientThread extends Thread {
    Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    private int threadId;

    ClientThread (Socket socket, int id) {
        this.socket = socket;
        this.threadId = id;
    }

    @Override
    public void run () {
        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(dis);
            oos = new ObjectOutputStream(dos);

            Request req = (Request) ois.readObject();
            requestProcess(req);
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Thread " + threadId + " is closed. ");
        }
        finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Thread " + threadId + " is closed. ");
        }
    }

    private void requestProcess(Request req) {
        Response res = null;
        Response.ResponseBuilder rb = Response.builder();

        try {
            res = requestBinding(req);
        }
        catch (BadRequestException e) {
            res = rb.status(ResponseState.CLIENT_ERROR)
                    .build();
        }
        catch (Exception e) {
            res = rb.status(ResponseState.SERVER_ERROR)
                    .build();
        }
        finally {
            try {
                oos.writeObject(res);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Response requestBinding(Request req) throws Exception {
        Class<?>[] prcClasses = {_user.class, _refrigerator.class, _ingredient.class, _recipe.class};
        Field[] codes = RequestCode.class.getFields();
        Field[] types = RequestType.class.getFields();

        byte codeL1 = (byte) (req.getCode() & 0xF0);
        byte codeL2 = (byte) (req.getCode() & 0xFF);
        byte type = req.getType();

        String packageName = "api";
        String codeL1Name = "", codeL2Name = "", typeName = "";
        String className =  "", methodName = "";
        for (Field f : types) {
            String fName = f.getName();
            byte fVal = (byte) f.get(RequestCode.class);
            if (type == fVal) {
                typeName = fName;
            }
        }
        for (Field f : codes) {
            String fName = f.getName();
            byte fVal = (byte) f.get(RequestCode.class);

            if (codeL1 == fVal) {
                codeL1Name = fName;
            }
            if (codeL2 == fVal) {
                codeL2Name = fName;
            }
        }

        className = packageName + "._" + NameFormat.convert2CamelCase(codeL1Name);
        methodName = "_" + NameFormat.convert2CamelCase(typeName+"_"+codeL2Name);

        for (Class<?> c : prcClasses) {
            if (c.getName().equals(className)) {
                for (Method m : c.getMethods()) {
                    if (m.getName().equals(methodName)) {
                        Response res = (Response) m.invoke(null, req);
                        System.out.printf("[REQ] status: %d info: %s-%s_%s\n", res.getStatus(), codeL1Name, typeName, codeL2Name);
                        return res;
                    }
                }
            }
        }
        throw new BadRequestException();
    }
}