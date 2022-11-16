import com.sun.jna.Platform;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Scanner;

public class MachineInfo {

  private static final boolean isARMWithParens() {
    return (Platform.ARCH.startsWith("arm") || Platform.ARCH.startsWith("aarch"));
  }

  private static final boolean isARMWithoutParens() {
    return Platform.ARCH.startsWith("arm") || Platform.ARCH.startsWith("aarch");
  }

  public static void main(String[] args) {
    System.out.println("--------------------------------------------");
    System.out.println("Output from Java System and Platform");
    System.out.println("--------------------------------------------");
    System.out.println("System.getProperty(\"os.name\"): " + System.getProperty("os.name"));
    System.out.println("System.getProperty(\"os.arch\"): " + System.getProperty("os.arch"));
    System.out.println("Platform.ARCH: " + Platform.ARCH);
    System.out.println("Platform.is64Bit(): " + Platform.is64Bit());
    System.out.println("Platform.isWindows(): " + Platform.isWindows());
    System.out.println("Platform.isMac(): " + Platform.isMac());
    System.out.println("Platform.isLinux(): " + Platform.isLinux());
    System.out.println("Platform.isIntel(): " + Platform.isWindows());
    System.out.println("Platform.ARCH.startsWith(\"arm\")): " + Platform.ARCH.startsWith("arm"));
    System.out.println("Platform.ARCH.startsWith(\"aarch\"): " + Platform.ARCH.startsWith("aarch"));
    System.out.println("Platform.isARM(): " + Platform.isARM());
    System.out.println("isARMWithoutParens: " + isARMWithoutParens());
    System.out.println("isARMWithParens: " + isARMWithParens());
    System.out.println("Platform.getOSType: " + Platform.getOSType());

    String[] commands = {"arch", "uname -amrs", "hostnamectl"};
    for (String cmd: commands) {
      try {
        System.out.println("--------------------------------------------");
        System.out.println("Output from: " + cmd);
        System.out.println("--------------------------------------------");

        String line;
        Process p = Runtime.getRuntime().exec(cmd);
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((line = input.readLine()) != null) {
          System.out.println(line);
        }
        input.close();

      }
      catch (IOException e) {
        System.out.println(cmd + " is not present or cannot be executed.");
      }
      catch (Exception e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
    }

    String[] files = {"/etc/os-release", "/etc/issue", "/proc/version"};
    for (String file: files) {
      try {
        System.out.println("--------------------------------------------");
        System.out.println("Reading from File: " + file);
        System.out.println("--------------------------------------------");

        File f = new File(file);
        Scanner scanner = new Scanner(f);
        while (scanner.hasNextLine()) {
          System.out.println(scanner.nextLine());
        }
        scanner.close();
      }
      catch (FileNotFoundException e) {
        System.out.println(file + " not found.");
      }
      catch (Exception e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
    }

    System.out.println("--------------------------------------------");
    System.out.println("Parsing from /etc/os-release");
    System.out.println("--------------------------------------------");
    Properties osReleaseProperties = new Properties();
    String file = "/etc/os-release";

    try {
      osReleaseProperties.load(new FileInputStream(file));

      String id = osReleaseProperties.getProperty("ID");
      String versionCodename = osReleaseProperties.getProperty("VERSION_CODENAME");

      System.out.println("ID: " + id + "<<<");
      System.out.println("VERSION_CODENAME: " + versionCodename + "<<<");
    }
    catch (FileNotFoundException e) {
      System.out.println(file + " not found.");
    }
    catch (Exception e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }
}

// On Debian Docker images
// -----------------------
// On tomcat:9.0.56-jre8 images
// root@2e634118ac03:~# cat /etc/issue
// Debian GNU/Linux 11 \n \l
// root@2e634118ac03:~# cat /etc/os-release
// PRETTY_NAME="Debian GNU/Linux 11 (bullseye)"
// NAME="Debian GNU/Linux"
// VERSION_ID="11"
// VERSION="11 (bullseye)"
// VERSION_CODENAME=bullseye
// ID=debian
// HOME_URL="https://www.debian.org/"
// SUPPORT_URL="https://www.debian.org/support"
// BUG_REPORT_URL="https://bugs.debian.org/"
// root@2e634118ac03:~# arch
// aarch64
