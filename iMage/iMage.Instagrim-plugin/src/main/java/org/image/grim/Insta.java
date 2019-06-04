package org.image.grim;

import javax.swing.JOptionPane;

import org.iMage.plugins.PluginForJmjrst;
import org.kohsuke.MetaInfServices;

/**
 * Instagrim Plugin
 * prints random Massages to syserr/out
 * @author Christopher Homberger
 */
@MetaInfServices
public class Insta extends PluginForJmjrst {
    private static String[] messages = new String[] {
        "Wow, scharfes Pic. Mit iMage erstellt?",
        "Wusste gar nicht, dass sowas mit iMage möglich ist. Voll krasse Farben.",
        "Voll billig :(",
        "Help, no Internet, Telecomm cooming sooooon!",
        "Foll schnell",
        "1224343 Bilder geschenkt!",
        "Thank you for paying us 1224343 Dollar",
        "Did not know that something with iMage is possible. Full of stark colors.",
        "Wow, sharp pic. Created with iMage?",
        "Vielen Dank, dass Sie uns 1224343,83 Dollar gezahlt haben",
        "Voll 1224343,642 km/h zu schnell"
    };

    public Insta() {

    }

    @Override
    public String getName() {
        return "instagrim";
    }

    @Override
    public void init(org.jis.Main main) {
        System.err.println("iMage: Der Bildverschönerer, dem Influencer vertrauen! Jetzt bist auch Du Teil unseres Teams, "
        + System.getProperty("user.name"));
    }

    @Override
    public void run() {
        System.out.println(messages[(int)(Math.random() * messages.length)]);
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

	@Override
	public void configure() {
        JOptionPane.showMessageDialog(null, String.join("\n", messages));
	}
}