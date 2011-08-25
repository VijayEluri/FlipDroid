import net.sf.json.groovy.JsonSlurper

def json = """[
    {
        "name": "爱范儿",
        "id": "1",
        "desc": "发现创新价值的科技媒体。 全景关注移动互联网，集中报道创业团队、最潮的智能手持及最酷的互联网应用，致力于“独立，前瞻，深入”的分析评论",
        "image_url": "http://bbs.ifanr.com/static/image/common/logo.png",
        "content_url":"http://www.ifanr.com/feed/rss",
        "cat":"互联网"
    },
    {
        "name": "分享网络2.0",
        "id": "2",
        "desc": "show web2.0 enchantment",
        "image_url": "http://www.showeb20.com/wp-content/temp/logo.png",
        "content_url":"http://feed.feedsky.com/showeb20",
        "cat":"互联网"
    },
    {
        "name": "新浪体育",
        "id": "3",
        "desc": "新浪体育新闻",
        "image_url": "http://www.36kr.com/wp-content/uploads/2011/04/36kr-Logo.png",
        "content_url":"http://rss.sina.com.cn/roll/sports/hot_roll.xml",
        "cat":"体育"
    },
    {
        "name": "36氪",
        "id": "4",
        "desc": "关注互联网创业",
        "image_url": "http://www.36kr.com/wp-content/uploads/2011/04/36kr-Logo.png",
        "content_url":"http://www.36kr.com/rss",
        "cat":"互联网"
    },
    {
        "name": "Technology Review(计算)",
        "id": "5",
        "desc": "麻省理工科技创业中文网",
        "image_url": "http://www.mittrchinese.com/img/logo.gif",
        "content_url":"http://www.mittrchinese.com/rssone.php?id=1",
        "cat":"科技"
    },
    {
        "name": "Technology Review(网络)",
        "id": "6",
        "desc": "麻省理工科技创业中文网",
        "image_url": "http://www.mittrchinese.com/img/logo.gif",
        "content_url":"http://www.mittrchinese.com/rssone.php?id=9",
        "cat":"科技"
    },
    {
        "name": "Technology Review(通讯)",
        "id": "7",
        "desc": "麻省理工科技创业中文网",
        "image_url": "http://www.mittrchinese.com/img/logo.gif",
        "content_url":"http://www.mittrchinese.com/rssone.php?id=10",
        "cat":"科技"
    }
    ,
    {
        "name": "Technology Review(能源)",
        "id": "8",
        "desc": "麻省理工科技创业中文网",
        "image_url": "http://www.mittrchinese.com/img/logo.gif",
        "content_url":"http://www.mittrchinese.com/rssone.php?id=4",
        "cat":"科技"
    }
    ,
    {
        "name": "Technology Review(新材料)",
        "id": "9",
        "desc": "麻省理工科技创业中文网",
        "image_url": "http://www.mittrchinese.com/img/logo.gif",
        "content_url":"http://www.mittrchinese.com/rssone.php?id=3",
        "cat":"科技"
    },
    {
        "name": "Technology Review(生物医药)",
        "id": "10",
        "desc": "麻省理工科技创业中文网",
        "image_url": "http://www.mittrchinese.com/img/logo.gif",
        "content_url":"http://www.mittrchinese.com/rssone.php?id=2",
        "cat":"科技"
    }
    ,
    {
        "name": "Technology Review(商务科技)",
        "id": "11",
        "desc": "麻省理工科技创业中文网",
        "image_url": "http://www.mittrchinese.com/img/logo.gif",
        "content_url":"http://www.mittrchinese.com/rssone.php?id=5",
        "cat":"科技"
    }
    ,
    {
        "name": "Technology Review(网络新闻精粹)",
        "id": "12",
        "desc": "麻省理工科技创业中文网",
        "image_url": "http://www.mittrchinese.com/img/logo.gif",
        "content_url":"http://www.mittrchinese.com/rssone.php?id=16" ,
        "cat":"科技"
    },
    {
        "name": "南方周末-热点新闻",
        "id": "13",
        "desc": "首页热点新闻",
        "image_url": "http://www.infzm.com/images/logo.png",
        "content_url":"http://www.infzm.com/rss/home/rss2.0.xml",
        "cat":"新闻"
    },
    {
        "name": "路透: 时事要闻",
        "id": "14",
        "desc": "路透中文网提供实时新闻，财经资讯和投资信息。路透社是全球最大的新闻通讯社之一，为全球媒体，金融实体，商业组织和个人提供新闻报道，金融资讯和相关技术方案。",
        "image_url": "http://cn.reuters.com/resources/images/reuters120.gif",
        "content_url":"http://cn.reuters.feedsportal.com/CNTopGenNews",
        "cat":"新闻"
    },
    {
        "name": "特写--华尔街日报",
        "id": "15",
        "desc": "《华尔街日报》中文网络版最新特写、深度报导及评论",
        "image_url": "",
        "content_url":"http://cn.wsj.com.feedsportal.com/c/33121/f/538761/index.rss",
        "cat":"新闻"
    },
    {
        "name": "南风窗",
        "id": "16",
        "desc": "",
        "image_url": "",
        "content_url":"http://blog.sina.com.cn/rss/1284797513.xml",
        "cat":"评论"
    },
    {
        "name": "参考消息",
        "id": "17",
        "desc": "",
        "image_url": "",
        "content_url":"http://www.ckxx.info/rss/rss.xml",
        "cat":"新闻"
    },
    {
        "name": "Leica中文摄影杂志",
        "id": "18",
        "desc": "...让你的摄影与众不同...",
        "image_url": "http://www.feedsky.com/feed/leica/sc/gif",
        "content_url":"http://feed.feedsky.com/leica",
        "cat":"摄影"
    }

    ,
    {
        "name": "外滩画报",
        "id": "19",
        "desc": "",
        "image_url": "",
        "content_url":"http://blog.sina.com.cn/rss/THEBUND.xml",
        "cat":"摄影"
    }
    ,
    {
        "name": "电影世界",
        "id": "20",
        "desc": "",
        "image_url": "",
        "content_url":"http://blog.sina.com.cn/rss/dianyingshijie.xml",
        "cat":"摄影"
    }
    ,
    {
        "name": "三联生活周刊",
        "id": "21",
        "desc": "",
        "image_url": "",
        "content_url":"http://blog.sina.com.cn/rss/1191965271.xml",
        "cat":"摄影"
    }
    ,
    {
        "name": "佳人",
        "id": "22",
        "desc": "",
        "image_url": "",
        "content_url":"http://jiaren.org/feed/",
        "cat":"摄影"
    }
    ,
    {
        "name": "《中国国家地理》的BLOG",
        "id": "26",
        "desc": "",
        "image_url": "",
        "content_url":"http://blog.sina.com.cn/rss/cng.xml",
        "cat":"读书"
    }
    ,
    {
        "name": "豆瓣最受欢迎的书评",
        "id": "27",
        "desc": "豆瓣成员投票选出的最佳书评",
        "image_url": "",
        "content_url":"http://www.douban.com/feed/review/book",
        "cat":"读书"
    }
    ,
    {
        "name": "精彩资讯-Mtime时光网",
        "id": "28",
        "desc": "时光网（Mtime.com）精彩资讯",
        "image_url": "",
        "content_url":"http://feed.mtime.com/news.rss",
        "cat":"读书"
    }
    ,
    {
        "name": "水煮沉浮",
        "id": "29",
        "desc": "水煮沉浮分享冷笑话大全、经典小笑话、经典爆笑语录和短笑话大全等新鲜资讯,善于挖掘冷笑话,发挥冷笑话功力,让喜欢笑话的人都happy.",
        "image_url": "",
        "content_url":"http://www.911usa.com.cn/happy/feed.php",
        "cat":"读书"
    }
    ,
    {
        "name": "美容护肤每日推荐",
        "id": "31",
        "desc": "介绍女性美容,护肤技巧的公益性站点——每日更新,欢迎转载！RSS输出全文,方便大家阅读，欢迎订阅!",
        "image_url": "http://www.feedsky.com/feed/meirong/sc/gif",
        "content_url":"http://feed.feedsky.com/meirong",
        "cat":"读书"
    }
    ,
    {
        "name": "论坛首页",
        "id": "32",
        "desc": "论坛首页- 丁香园论坛 - 医学/药学/生命科学论坛",
        "image_url": "http://www.dxy.cn/bbs/images/title.gif",
        "content_url":"http://www.dxy.cn/bbs/rss/2.0/all.xml",
        "cat":"医学"
    }
    ,
    {
        "name": "环球科学（科学美国人）",
        "id": "33",
        "desc": "",
        "image_url": "",
        "content_url":"http://blog.sina.com.cn/rss/sciam.xml",
        "cat":"科学"
    }
    ,
    {
        "name": "NatureNews - Most recent articles - nature.com science feeds",
        "id": "35",
        "desc": "Nature - the world's best science and medicine on your desktop",
        "image_url": "http://www.nature.com/includes/rj_globnavimages/news_logo.gif",
        "content_url":"http://feeds.nature.com/news/rss/most_recent",
        "cat":"科学"
    }

    ,
    {
        "name": "AppShopper.com: Popular Price Changes (Free) for iOS",
        "id": "36",
        "desc": "App Discovery",
        "image_url": "",
        "content_url":"http://appshopper.com/feed/?mode=featured&filter=price&type=free",
        "cat":"移动应用"
    }
    ,
    {
        "name": "善用佳软",
        "id": "37",
        "desc": "http://xbeta.info",
        "image_url": "http://www.feedsky.com/feed/xbeta/sc/gif",
        "content_url":"http://feed.xbeta.info/",
        "cat":"软件"
    }
    ,
    {
        "name": "首页推荐",
        "id": "38",
        "desc": "首页推荐",
        "image_url": "",
        "content_url":"http://www.xitek.com/newsite/plus/rss.php?tid=14",
        "cat":"摄影"
    }
    ,
    {
        "name": "精品绿色便携软件",
        "id": "39",
        "desc": "追求绿色便携理念，打造清爽干净系统！",
        "image_url": "",
        "content_url":"http://www.portablesoft.org/feed/",
        "cat":"软件"
    }
    ,
    {
        "name": "Engadget",
        "id": "40",
        "desc": "Engadget",
        "image_url": "http://www.blogsmithmedia.com/www.engadget.com/media/feedlogo.gif",
        "content_url":"http://www.engadget.com/rss.xml",
        "cat":"科技"
    }
    ,
    {
        "name": "小众软件 - Appinn",
        "id": "41",
        "desc": "分享免费、小巧、实用、有趣、绿色的软件",
        "image_url": "",
        "content_url":"http://www.appinn.com/feed/",
        "cat":"软件"
    }
    ,
    {
        "name": "cnBeta.COM",
        "id": "42",
        "desc": "cnBeta.COM - 简明IT新闻,网友媒体与言论平台",
        "image_url": "http://www.cnbeta.com/images/logo.gif",
        "content_url":"http://www.cnbeta.com/backend.php",
        "cat":"IT"
    }
    ,
    {
        "name": "Engadget 中国版",
        "id": "43",
        "desc": "Engadget 中国版",
        "image_url": "http://cn.engadget.com/media/feedlogo.gif",
        "content_url":"http://cn.engadget.com/rss.xml",
        "cat":"科技"
    }
    ,
    {
        "name": "Chrome OS Fans",
        "id": "44",
        "desc": "Just another WordPress weblog",
        "image_url": "",
        "content_url":"http://www.chromeosfans.com/feed",
        "cat":"浏览器"
    }
    ,
    {
        "name": "VantagePoint",
        "id": "45",
        "desc": "Scivantage's corporate weblog about our products, services and our thoughts on issues affecting the retail brokerage market.",
        "image_url": "",
        "content_url":"http://www.scivantage.com/vantagepoint/?feed=rss2",
        "cat":"浏览器"
    }
    ,
    {
        "name": "VantagePoint",
        "id": "46",
        "desc": "Scivantage's corporate weblog about our products, services and our thoughts on issues affecting the retail brokerage market.",
        "image_url": "",
        "content_url":"http://www.scivantage.com/vantagepoint/?feed=rss2",
        "cat":"浏览器"
    }
    ,
    {
        "name": "草根网",
        "id": "47",
        "desc": "草根网关注互联网行业变化，关注草根创业，关注项目的运营，草根网是以DIGG +点评方式阅读IT资讯，交流思想，积累知识的学习型互助社区。",
        "image_url": "http://www.20ju.com/images/logo_cg.gif",
        "content_url":"http://www.20ju.com/rss.xml",
        "cat":"草根"
    }

,{
        "name": "Tech2IPO",
        "id": "48",
        "desc": "科技创新创业平台",
        "image_url": "",
        "content_url":"http://tech2ipo.com/feed/rss/",
        "cat":"互联网"
    }
,{
        "name": "互联网的那点事...",
        "id": "49",
        "desc": "隐居在沙漠里的blog，互联网的那点事......",
        "image_url": "http://www.feedsky.com/feed/alibuybuy/sc/gif",
        "content_url":"http://feed.feedsky.com/alibuybuy",
        "cat":"互联网"
    }]"""

def rss = new JsonSlurper().parseText(json)
rss.each{
    println it.content_url
}