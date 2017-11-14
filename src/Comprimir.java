
import static com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler.BUFFER_SIZE;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.SwingWorker;

public class Comprimir extends SwingWorker<Object, String[]> {

    private String inputFolder;
    private String outputFolder;
    private final int BUFFER_SIZE = 1024;
    public Comprimir(String inpurFolder, String outputFolder){
        this.inputFolder=inpurFolder;
        this.outputFolder=outputFolder;
    }
    
    @Override
    protected Object doInBackground() throws Exception {
        zip();
        
        return new Object();
        
    }

    public void zip() throws FileNotFoundException, IOException {
        File folder = new File(inputFolder);
        File[] listOfFiles = folder.listFiles();
        List<String> files = new ArrayList<>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if(listOfFiles[i].isFile()) files.add(listOfFiles[i].getAbsolutePath());            
        }
        
        try {
            BufferedInputStream origin;

            FileOutputStream dest = new FileOutputStream(outputFolder+"\\folder.zip");
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            int zippedFiles = 0;
            byte[] data = new byte[BUFFER_SIZE];
            Iterator i = files.iterator();
            while (i.hasNext()) {
                
                String filename = (String) i.next();
                FileInputStream fi = new FileInputStream(filename);
                origin = new BufferedInputStream(fi, BUFFER_SIZE);

                ZipEntry entry = new ZipEntry(filename.substring(filename.lastIndexOf('\\')+1));
                
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER_SIZE)) >0) {
                    out.write(data, 0, count);
                }
                out.closeEntry();
                origin.close();
                zippedFiles++;
                setProgress(100*zippedFiles/files.size());
                firePropertyChange("progress", 100*(zippedFiles-1)/files.size(), 100*zippedFiles/files.size());
            }
            
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
