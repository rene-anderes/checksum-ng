package org.anderes.edu.checksum;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import static com.google.common.base.Preconditions.*;

public class TheSHA1Calculator implements TheCalculator {

    private final HexBinaryAdapter hexBinaryAdapter = new HexBinaryAdapter();

    @Override
    public ResultData eval(final Path theFile) throws FileNotFoundException, IOException {
        checkNotNull(theFile);
        
        try (final InputStream input = Files.newInputStream(theFile)) {
            
            final MessageDigest sha1 = MessageDigest.getInstance("SHA-1");

            byte[] buffer = new byte[8192];
            int len = input.read(buffer);

            while (len != -1) {
                sha1.update(buffer, 0, len);
                len = input.read(buffer);
            }

            final String marshal = hexBinaryAdapter.marshal(sha1.digest());
            return new ResultData(theFile, marshal);
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
            System.exit(99);
            return null;
        } 
    }

}
