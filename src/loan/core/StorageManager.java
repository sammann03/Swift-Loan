package loan.core;

import java.io.*;

public class StorageManager {
    public static <T extends Serializable> void saveData(String fileName, T object){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))){
            oos.writeObject(object);
            System.out.println("Data saved to: " + fileName);
        }
        catch(IOException e){
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public static <T extends Serializable> T loadData(String fileName){
        File file = new File(fileName);
        if(!file.exists()) return null;

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))){
            return (T) ois.readObject();
        }
        catch(IOException | ClassNotFoundException e){
            System.out.println("Error loading data: " + e.getMessage());
            return null;
        }
    }
}
