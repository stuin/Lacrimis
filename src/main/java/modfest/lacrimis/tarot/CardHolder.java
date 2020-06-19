package modfest.lacrimis.tarot;

public interface CardHolder {
    void addCard(TarotCardType type);
    TarotCardType[] getCards();
}
