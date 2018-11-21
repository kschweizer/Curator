import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class UserLibrary {
    private JSONArray[] items;
    private JSONObject user;

    public UserLibrary(JSONObject user, JSONArray[] items) {
        this.items = items;
        this.user = user;
    }

    public JSONArray[] getItems() {
        return this.items;
    }

    public void writeToFile() throws Exception {
        String currentPath = System.getProperty("user.dir");
        String path = currentPath + "/users/" + this.user.get("display_name") + this.user.get("id");
        String[] itemStrings = new String[this.items.length];
        for (int i = 0; i < itemStrings.length; i++) {
            itemStrings[i] = this.items[i].toString();
        }
        String userString = this.user.toString();
        File tmpDir = new File(path);
        boolean exists = tmpDir.exists();
        // if the directory exists then there should already be items and user info text files unless the directory was manually modified
        writeDirectory(path, itemStrings, userString, exists);
    }



    // RETURNS: a set of JSONObject tracks that are shared between both libraries
    /*public Set compareLibraries(UserLibrary otherLibrary) throws Exception {
        JSONArray items1 = this.items;
        JSONArray items2 = otherLibrary.getItems();
        Set<JSONObject> itemSet1 = buildTrackSet(items1);
        Set<JSONObject> itemSet2 = buildTrackSet(items2);
        return intersectionSet(itemSet1, itemSet2);
    } */

    // helper to build a Set of JSONObject tracks given a JSONArray of items
    private Set<JSONObject> buildTrackSet(JSONArray items) throws Exception {
        Set<JSONObject> itemSet = new HashSet<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject track = (JSONObject) items.get(i);
            track = (JSONObject) track.get("track");
            itemSet.add(track);
        }
        return itemSet;
    }

    // Finds the intersection of two sets of JSONObject elements
    private Set<JSONObject> intersectionSet(Set<JSONObject> set1, Set<JSONObject> set2) {
        Set<JSONObject> smallerSet;
        Set<JSONObject> largerSet;
        Set<JSONObject> intersection = new HashSet<>();      // what we will return
        if (set1.size() <= set2.size()) {
            smallerSet = set1;
            largerSet = set2;
        }   else {
            smallerSet = set2;
            largerSet = set1;
        }

        Iterator smallSetIterator = smallerSet.iterator();
        while (smallSetIterator.hasNext()) {
            JSONObject track = (JSONObject) smallSetIterator.next();
            if (largerSet.contains(track)) {
                intersection.add(track);
            }
        }
        return intersection;
    }

    private void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) {
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    private void writeDirectory(String path, String[] itemStrings, String userString, Boolean exists) throws Exception {
        if (exists) {
            // clear the directory so we can update it
            File pathFolder = new File(path);
            deleteFolder(pathFolder);
        }

        Files.createDirectories(Paths.get(path));

        for (int i = 0; i < itemStrings.length; i++) {
            String index = Integer.toString(i);
            File itemsFile = new File(path + "/items" + index + ".json");
            itemsFile.createNewFile();
            FileWriter itemsWriter = new FileWriter(itemsFile, false);
            itemsWriter.write(itemStrings[i]);
            itemsWriter.close();
        }

        File userFile = new File(path + "/user.json");
        userFile.createNewFile();
        FileWriter userWriter = new FileWriter(userFile, false);
        userWriter.write(userString);
        userWriter.close();

        if (exists) {
            System.out.println("Library for " + this.user.get("display_name") + " successfully updated");
        }
        else {
            System.out.println("Created new library entry for " + this.user.get("display_name") + " in /users/");
        }
    }

}
