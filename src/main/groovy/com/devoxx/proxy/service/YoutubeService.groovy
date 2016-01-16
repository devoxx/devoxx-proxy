package com.devoxx.proxy.service

import com.devoxx.proxy.youtube.Auth
import com.devoxx.proxy.youtube.YoutubeVideo
import com.google.api.client.http.HttpRequest
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.ResourceId
import com.google.api.services.youtube.model.SearchListResponse
import com.google.api.services.youtube.model.SearchResult
import com.google.api.services.youtube.model.Thumbnail
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Service

/**
 * Created by sarbogast on 15/01/2016.
 */
@Service
@ConfigurationProperties(prefix = "youtube")
class YoutubeService {

    @Value('${youtube.apiKey}')
    String apiKey
    int videosReturned

    private YouTube youtube

    public YoutubeService() {
        youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName("tvoxx-server").build();
    }

    YoutubeVideo getYoutubeUrl(String title, String talkType, String channelId) {

        // Prompt the user to enter a query term.
        String queryTerm = title

        // Define the API request for retrieving search results.
        YouTube.Search.List search = youtube.search().list("id,snippet");

        // Set your developer key from the {{ Google Cloud Console }} for
        // non-authenticated requests. See:
        // {{ https://cloud.google.com/console }}
        search.setKey(apiKey)
        search.setQ(queryTerm)

        // Restrict the search results to only include videos. See:
        // https://developers.google.com/youtube/v3/docs/search/list#type
        search.setType("video");

        // To increase efficiency, only retrieve the fields that the
        // application uses.
        search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url,snippet/thumbnails/high/url,snippet/channelId)");
        search.setMaxResults(videosReturned);

        // Call the API and print results.
        SearchListResponse searchResponse = search.execute();
        List<SearchResult> searchResultList = searchResponse.getItems();
        if (searchResultList != null && searchResultList.size() > 0) {
            for(singleVideo in searchResultList) {
                ResourceId resourceId = singleVideo.getId()

                if (resourceId.getKind().equals("youtube#video") && (channelId != null && channelId == singleVideo.getSnippet().getChannelId())) {
                    def youtubeVideo = new YoutubeVideo()
                    def thumbnails = singleVideo.getSnippet().getThumbnails()
                    Thumbnail thumbnail = thumbnails.getHigh()?:thumbnails.getDefault()
                    youtubeVideo.thumbnailUrl = thumbnail.getUrl()
                    youtubeVideo.videoId = resourceId.getVideoId()
                    return youtubeVideo
                }
            }
            return null
        } else {
            return null
        }
    }

}
