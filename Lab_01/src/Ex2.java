public class Ex2 {
    public static void main(String[] args) {

        justifyText(new String[]{"The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog."}, 11);
        //justifyText(new String[]{"Science", "is", "what", "we", "understand", "well", "enough", "to", "explain", "to", "a", "computer."}, 20);

    }

    public static String[] justifyText(String[] words, int maxWidth) {
        String[] result = new String[words.length];// za da ne go urazmerqvame


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

            if(numberOfWords==1|| (i==words.length-1)){

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


            }else{
                int endLength = currentSum + numberOfWords;
                //int wsCount = maxWidth - currentSum - numberOfWords + 1;// inbetween 2 words there is one ws
                int wsCount = maxWidth - currentSum;
                int padding = wsCount / (numberOfWords - 1);
                int remainder = wsCount % (numberOfWords - 1);



                for (int j = begin; j <= i; j++) {
                    str.append(words[j]);
                    for (int t = 0; t < padding; t++) {
                        str.append(' ');
                    }
                    if (remainder > 0) {
                        str.append(' ');
                        remainder--;
                    }
                }
            }
            System.out.println(str.toString());
        }

        return result;
    }
}
