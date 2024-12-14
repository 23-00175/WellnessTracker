package utils;

public class WellnessQuotes {
    public enum Quote {
        QUOTE_ONE("Wellness is not a medical fix but a way of living - a lifestyle sensitive and responsive to all the dimensions of body, mind, and spirit, an approach to life we each design to achieve our highest potential for well-being now and forever.", "Greg Anderson"),
        QUOTE_TWO("Wellness encompasses a healthy body, a sound mind, and a tranquil spirit. Enjoy the journey as you strive for wellness.", "Laurette Gagnon Beaulieu"),
        QUOTE_THREE("Our bodies are our gardens - our will are our gardeners", "William Shakespeare"),
        QUOTE_FOUR("Self-care is not selfish. You cannot serve from an empty vessel.", "Eleanor Brown"),
        QUOTE_FIVE("Today is your day to start fresh, to eat right, to train hard, to live healthy, to be proud.", "Bonnie Pfiester"),
        QUOTE_SIX("Your body holds deep wisdom. Trust in it. Learn from it. Nourish it. Watch your life transform and be healthy", "Bella Bleue"),
        QUOTE_SEVEN("Wellness is the complete integration of body, mind, and spirit - the realization that everything we do, think, feel, and believe has an effect on our state of well-being.", "Greg Anderson"),
        QUOTE_EIGHT("If you don\'t do what\'s best for your body, you\'re the one who comes up on the short end.", "Julius Erving"),
        QUOTE_NINE("A healthy lifestyle is the most potent medicine at your disposal.", "Sravani Saha Nakhro"),
        QUOTE_TEN("To ensure good health: eat lightly, breathe deeply, live moderately, cultivate cheerfulness, and maintain an interest in life.", "William Londen");

        private final String text;
        private final String author;

        Quote(String text, String author) {
            this.text = text;
            this.author = author;
        }

        public String getText() {
            return text;
        }

        public String getAuthor() {
            return author;
        }
    }
}
