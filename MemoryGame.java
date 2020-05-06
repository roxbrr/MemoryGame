
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MemoryGame extends JFrame implements ActionListener
{

    private static final String[] CARD_NAMES = {"bear.png", "bird.png", "cow.png", "cat.png",
        "deer.png", "fox.png", "owl.png", "squirrel.png"};

    private int numAttempts;
    private int numMatches;

    private Card[] cards;
    private JPanel cardsPanel;
    private JLabel attemptsLabel;
    private JLabel matchesLabel;

    private Card firstOpenedCard;
    private Card secondOpenedCard;

    private JButton continueButton;
    private JButton exitButton;

    public MemoryGame()     //Initialize the board, the cards, and the labels
    {
        setTitle("Memory Game");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(500, 500);
        setLocationRelativeTo(null);


        attemptsLabel = new JLabel();       //Initialize labels to indicate game stats
        matchesLabel = new JLabel();

        JPanel gameStatsPanel = new JPanel(new FlowLayout());
        gameStatsPanel.add(attemptsLabel);
        gameStatsPanel.add(matchesLabel);
        add(BorderLayout.NORTH, gameStatsPanel);


        cards = new Card[CARD_NAMES.length * 2];        //Initialize the cards to use (a pair each)

        for (int i = 0, j = 0; i < CARD_NAMES.length; i++)
        {
            cards[j++] = new Card("images/" + CARD_NAMES[i]);
            cards[j++] = new Card("images/" + CARD_NAMES[i]);
        }


        continueButton = new JButton("Continue");       //Initialize the buttons
        exitButton = new JButton("Exit");

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.add(continueButton);
        buttonsPanel.add(exitButton);
        add(BorderLayout.SOUTH, buttonsPanel);

        continueButton.addActionListener(this);
        exitButton.addActionListener(this);


        initializeNewGame();        //Automatically starts a new game

    }

    private void initializeNewGame()        //Shuffles cards and puts them into the cards panel
    {

        if (cardsPanel != null)     //Remove old cards in the user interface
        {
            remove(cardsPanel);
        }

        continueButton.setEnabled(false);

        numAttempts = 0;
        numMatches = 0;

        attemptsLabel.setText("Attempts: 0");
        matchesLabel.setText("Matches: 0");

        for (int i = 0; i < cards.length; i++)      //Shuffle the cards
        {
            int r = i + (int) (Math.random() * (cards.length - i));
            Card temp = cards[r];
            cards[r] = cards[i];
            cards[i] = temp;
        }

        cardsPanel = new JPanel(new GridLayout(cards.length / 4, cards.length / 4));        //Put the cards in the user interface arranged as 4 x 4

        for (Card card : cards)
        {
            card.addActionListener(this);
            card.hideCard();
            cardsPanel.add(card);
        }

        add(BorderLayout.CENTER, cardsPanel);

        firstOpenedCard = null;
        secondOpenedCard = null;

        this.repaint();
        this.revalidate();
    }

    private void evaluateScore()        //Evaluate the score of the user
    {
        int score = 0;

        if (numAttempts > 0)
        {
            score = (int) (((double) numMatches / (double) numAttempts) * 100);
        }

        String result = "Your score is " + score + ", ";

        if (score > 97)
        {
            result += "AMAZING!";
        } else if (score > 80)
        {
            result += "Excellent!";
        } else if (score > 60)
        {
            result += "OK!";
        } else if (score > 30)
        {
            result += "Lacking!";
        } else if (score > 10)
        {
            result += "Suffering!";
        } else
            {
            result += "Mind has escaped...";
            }

        JOptionPane.showMessageDialog(this, result);
    }


    private void evaluateOpenedCards()          //Evaluate and score the opened cards
    {
        if (firstOpenedCard == null || secondOpenedCard == null)        //Evaluation requires 2 selected cards
        {
            return;
        }

        numAttempts++;


        if (firstOpenedCard.equals(secondOpenedCard))           //Check for matches
        {
            numMatches++;

            firstOpenedCard = null;
            secondOpenedCard = null;
        } else
            {
            continueButton.setEnabled(true);            //Continue button only works for mismatched cards
            }

        attemptsLabel.setText("Attempts: " + numAttempts);      //Update the stats
        matchesLabel.setText("Matches: " + numMatches);

        if (numMatches >= CARD_NAMES.length)            //If all were matched then evaluate the score
        {
            evaluateScore();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)          //Handle button events including the cards
    {
        if (e.getSource() instanceof Card)
        {
            if (firstOpenedCard != null && secondOpenedCard != null)                   //Player can only select a new card as long as there's no
            {                                                                          //2 cards selected yet
                return;
            }

            if (firstOpenedCard == null)            //When card is clicked, select and evaluate
            {
                firstOpenedCard = (Card) e.getSource();
                firstOpenedCard.showCard();
                firstOpenedCard.removeActionListener(this);
            } else if (secondOpenedCard == null)
            {
                secondOpenedCard = (Card) e.getSource();
                secondOpenedCard.showCard();
                secondOpenedCard.removeActionListener(this);
            }

            evaluateOpenedCards();
        } else if (e.getSource() == continueButton)
        {
            firstOpenedCard.hideCard();             // Continue button will hide the opened cards
            secondOpenedCard.hideCard();

            firstOpenedCard.addActionListener(this);
            secondOpenedCard.addActionListener(this);

            firstOpenedCard = null;
            secondOpenedCard = null;

            continueButton.setEnabled(false);
        } else if (e.getSource() == exitButton)     //Close program on confirmation
        {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to end the game?") != JOptionPane.YES_OPTION)
            {
                return;
            }

            if (numMatches < CARD_NAMES.length)     //Perform evaluation
            {
                evaluateScore();
            }

            setVisible(false);
            dispose();
        }
    }


    public static void main(String[] args)          //Entry point of the program, creates instance of memory game and starts
    {
        new MemoryGame().setVisible(true);
    }
}
