class Solution {
public int[] findOrder(int numCourses, int[][] prerequisites) {

        Map<Integer, List<Integer>> adjList = new HashMap<>();
        for(int i = 0; i < numCourses; i++){
            adjList.put(i, new ArrayList<>());
        }

        int[] indegree = new int[numCourses];
        for(int[] pair : prerequisites){
            adjList.get(pair[1]).add(pair[0]);
            indegree[pair[0]]++;
        }

        Deque<Integer> queue = new ArrayDeque<>();
        for(int i = 0; i < numCourses; i++){
            if(indegree[i] == 0)
                queue.addLast(i);
        }

        int completed = 0;
        int idx = 0;
        int[] orderOfCourses = new int[numCourses];
        while(!queue.isEmpty()){
            int curr = queue.pollFirst();
            orderOfCourses[idx++] = curr;
            completed++;
            for(int nei : adjList.get(curr)){
                indegree[nei]--;
                if(indegree[nei] == 0){
                    queue.add(nei);
                }
            }
        }

        return completed == numCourses ? orderOfCourses : new int[0];
    }
}