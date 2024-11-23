public class TextJustifier {
    public static void main(String[] args) {

        String[]print=justifyText(new String[]{"The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog."}, 11);
        //String[] print = justifyText(new String[]{"Science", "is", "what", "we", "understand", "well", "enough", "to", "explain", "to", "a", "computer."}, 20);

        for (String str : print) {
            System.out.println(str);
        }
    }

    public static String[] justifyText(String[] words, int maxWidth) {
        String[] result = new String[words.length];
        int resultSize = 0;

        for (int i = 0; i < words.length; i++) {
            int currentSum = words[i].length();
            int numberOfWords = 1;
            int begin = i;
            while (i + 1 < words.length && currentSum + words[i + 1].length() + numberOfWords <= maxWidth) {
                currentSum += words[i + 1].length();
                numberOfWords++;
                i++;
            }

            StringBuilder str = new StringBuilder();

            if (numberOfWords == 1 || (i == words.length - 1)) {

//                str.append(words[begin]);
//                for(int t=0;t<maxWidth-currentSum;t++){
//                    str.append(' ');
//                }
                for (int j = begin; j <= i; j++) {
                    str.append(words[j]);
                    if (j < i) {
                        str.append(' ');
                    }
                }

                while (str.length()!=maxWidth){
                    str.append(' ');
                }


            } else {
                int endLength = currentSum + numberOfWords;
                //int wsCount = maxWidth - currentSum - numberOfWords + 1;// inbetween 2 words there is one ws
                int wsCount = maxWidth - currentSum;
                int padding = wsCount / (numberOfWords - 1);
                int remainder = wsCount % (numberOfWords - 1);


                for (int j = begin; j <= i; j++) {
                    str.append(words[j]);
                    if(j<i){
                    for (int t = 0; t < padding; t++) {
                        str.append(' ');
                    }
                    if (remainder > 0) {
                        str.append(' ');
                        remainder--;
                    }
                    }
                }
            }
            result[resultSize] = str.toString();
            resultSize++;
            //System.out.println(str.toString());
        }

        // todo: return the

        String[] toReturn = new String[resultSize];
        for (int i = 0; i < resultSize; i++) {
            toReturn[i] = result[i];
        }

        return toReturn;
    }
}
