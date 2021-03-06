package com.goal98.flipdroid2.model;


import com.goal98.flipdroid2.exception.NoMorePageException;
import com.goal98.flipdroid2.exception.NoMoreStatusException;
import com.goal98.flipdroid2.view.Page;
import com.goal98.flipdroid2.view.PagingStrategy;
import com.srz.androidtools.autoloadlistview.NoSuchPageException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class ContentRepo {

    private ArticleSource articleSource;

    private PagingStrategy pagingStrategy;

    private PagedPageView pagedList;

    private UnPagedArticles unPagedArticles;
    private ContentCache contentCache;
    private ExecutorService executor;

    public int getRefreshingToken() {
        return contentCache.getRefreshingToken();
    }

    public int getTotal(){
        return pagedList.size();
    }

    private Map<Integer, Future<Page>> futureMap = new HashMap<Integer, Future<Page>>();

    public ContentRepo(PagingStrategy pagingStrategy, Semaphore refreshingSemaphore) {
        pagedList = new PagedPageView();
        unPagedArticles = new UnPagedArticles(new ArrayList<Article>());
        contentCache = new ContentCache(pagedList, unPagedArticles, pagingStrategy, refreshingSemaphore);
        executor = Executors.newCachedThreadPool();
    }

    public ArticleSource getArticleSource() {
        return articleSource;
    }

    public void setArticleSource(ArticleSource articleSource) {
        this.articleSource = articleSource;
        contentCache.setArticleSource(articleSource);
    }

    public PagingStrategy getPagingStrategy() {
        return pagingStrategy;
    }

    public void setPagingStrategy(PagingStrategy pagingStrategy) {
        this.pagingStrategy = pagingStrategy;
    }

    public ContentCache getContentCache() {
        return contentCache;
    }

    //同时只能一个线程会调用该方法，第三页没拿到不能拿第四页
    public synchronized Page getPage(final int pageNo) throws NoMorePageException, ExecutionException, InterruptedException, NoSuchPageException {
        if (contentCache.getPageCacheTo() < pageNo) {//还没取到，或者取到了，没放倒cache里去
            if (futureMap.containsKey(pageNo)) {//看看是不是在在futureMap里，是的话就等他取好
                //Log.d("cache system", "waiting smartPage " + pageNo + " to load");
                Page smartPage = futureMap.get(pageNo).get();
                //contentCache.addPage(smartPage);
                futureMap.remove(pageNo);
                return smartPage;
            }
        }
        //Log.d("cache system", "looking repo for " + pageNo + " to load");
        Page smartPage = contentCache.getPage(pageNo);
        int preloadPageNo = pageNo + 3;

        //Log.d("cache system", "checking if smartPage " + preloadPageNo + " need to be loaded");
        if (preloadPageNo > contentCache.getPageCacheTo() && !futureMap.containsKey(preloadPageNo)) {  //看到第8页的时候，去看是不是cache有第10页
            preload(preloadPageNo);
        }
        //Log.d("cache system", "returning smartPage" + pageNo);
        return smartPage;
    }

    private void preload(final int pageNo) {
        //Log.d("cache system", "preloading pageContainer " + pageNo);
        Future future = executor.submit(new Callable() {
            public Object call() throws Exception {
                Page preloadedSmartPage = contentCache.getPagePreload(pageNo);
                //Log.d("cache system", "preload pageContainer " + pageNo + " done");
                return preloadedSmartPage;
            }
        });
        futureMap.put(pageNo, future);
    }

    public synchronized void refreshAndPage(int token) throws NoMoreStatusException {
        refresh(token);
        contentCache.pageAfterRefresh();
    }

    public synchronized void refresh(int token) throws NoMoreStatusException {
        contentCache.refresh(token);
    }
}
