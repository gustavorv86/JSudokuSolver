
package frames;

import javax.swing.JOptionPane;

public class Messages {
    
    public static void showInfo(String message){
        JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void showError(String message){
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static boolean showOption(String title, String message){
        int reply = JOptionPane.showConfirmDialog(
                null, message,
                title, JOptionPane.YES_NO_OPTION
        );
        return reply == JOptionPane.YES_OPTION;
    }
}
