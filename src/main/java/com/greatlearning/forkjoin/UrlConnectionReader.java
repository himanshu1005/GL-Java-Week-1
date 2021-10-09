package com.greatlearning.forkjoin;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class UrlConnectionReader extends RecursiveTask<String> {
    int allUrl = 0;
    private String[] urls;
    private String url;

    public UrlConnectionReader(String[] urls) {
        this.urls = urls;
        allUrl = urls.length;
    }
    public UrlConnectionReader(String url) {
        this.url = url;
        allUrl = 1;
    }

    @Override
    protected String compute() {
        if (allUrl > 1) {
            List<UrlConnectionReader> subtasks = new ArrayList<>();
            subtasks.addAll(createSubTasks());
//            Forking the subtasks
            for (UrlConnectionReader subtask : subtasks) {
                subtask.fork();
            }
            String output = "";
            for (UrlConnectionReader subtask : subtasks) {
                output += subtask.join();
            }
            return output;
        } else {
            readDataFromUrlAndWriteToFile(url);
        }
        return url + " Url data fetched successfully. \n";
    }

    private Collection<? extends UrlConnectionReader> createSubTasks() {
        List<UrlConnectionReader> subtasks = new ArrayList<>();
//        Creating subtask for each url
        for (String url : urls) {
            UrlConnectionReader subtask1 = new UrlConnectionReader(url);
            subtasks.add(subtask1);
        }
        return subtasks;
    }

    private void readDataFromUrlAndWriteToFile(String urlStr) {
        try {
            URL url = new URL(urlStr);
            try {
                URLConnection urlConnection = url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                Read the URL content
                String content = bufferedReader.lines().parallel().collect(Collectors.joining("\n"));
//                Store the output in output.text file
                PrintWriter printWriter = new PrintWriter(new File(System.getProperty("user.dir")+"\\output\\output.txt"));
//                printWriter.write(content);
//                Crop the HTML
                printWriter.write(content.replaceAll("\\<(/?[^\\>]+)\\>", "\\ ").replaceAll("\\s+", " ").trim());
//                Close the connections
                printWriter.flush();
                printWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
