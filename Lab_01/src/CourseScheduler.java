public class CourseScheduler{
    public static void main(String[] args) {

//        System.out.println(maxNonOverlappingCourses(new int[][]{{9, 11}, {10, 12}, {11, 13}, {15, 16}}));
//        System.out.println(maxNonOverlappingCourses(new int[][]{{19, 22}, {17, 19}, {9, 12}, {9, 11}, {15, 17}, {15, 17}}));
//        System.out.println(maxNonOverlappingCourses(new int[][]{{19, 22}}));
//        System.out.println(maxNonOverlappingCourses(new int[][]{{13, 15}, {13, 17}, {11, 17}}));
    }

    public static void customBubble(int[][] arr) {

        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i][1] > arr[j][1]) {
                    int[] tmp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = tmp;
                }
            }
        }

    }

    public static int maxNonOverlappingCourses(int[][] courses) {
        int result = 0;
        int maxTime = Integer.MIN_VALUE;

        customBubble(courses);

        for (int[] course : courses) {
            if (course[0] >= maxTime) {
                result++;
                maxTime = course[1];
            }
        }

        return result;
    }

}