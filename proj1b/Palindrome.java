public class Palindrome {
    public Deque<Character> wordToDeque(String word){
        LinkedListDeque<Character> L = new LinkedListDeque<>();
        for(int i = 0;i < word.length();i ++){
            L.addLast(word.charAt(i));
        }
        return L;
    }

    public boolean isPalindrome(String word){
        if(word.length() <= 1){
            return true;
        }
        Deque<Character> L = wordToDeque(word);
        int flag = 0;
        for(int i = 0;i < word.length();i ++) {
            if(L.get(i) == L.get(word.length() -1 - i)){
                flag ++;
            }
        }
        if(flag == word.length()){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        if(word.length() <= 1){
            return true;
        }
        int flag = 0;
        for(int i = 0;i < word.length()/2;i ++){
            if(cc.equalChars(word.charAt(i),word.charAt(word.length() - 1 - i))) {
                flag ++;
            }
        }
        if(flag == word.length()/2) {
            return true;
        }
        else {
            return false;
        }
    }
}
