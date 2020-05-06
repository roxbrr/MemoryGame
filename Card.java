
import javax.swing.*;
import java.awt.*;

public class Card extends JButton
{

    private ImageIcon imageIcon;
    private String imagePath;

    public Card()               // Creates an empty card
    {
        imageIcon = null;
        imagePath = "";
    }

    public Card(String imagePath)           //Initialize image to be used by button
    {
        this.imagePath = imagePath;
        imageIcon = new ImageIcon(imagePath);
        imageIcon = new ImageIcon(((Image) imageIcon.getImage()).getScaledInstance(70, 70, Image.SCALE_FAST));
    }

    public void hideCard()      //Removes image from card
    {
        setIcon(null);
    }

    public void showCard()      //Show image of the card
    {
        setIcon(imageIcon);
    }

    @Override
    public boolean equals(Object o)         //Check for card matches
    {
        if (!(o instanceof Card))
        {
            return false;
        }

        Card other = (Card) o;
        return imagePath.equals(other.imagePath);
    }
}
