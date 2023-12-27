package com.example.lab1;

import static java.security.AccessController.getContext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NewsListFragment.OnNewsItemSelectedListener {

    private NewsDetailFragment newsDetailFragment;
    private NetworkChangeReceiver receiver;

    private DirectoryBroadcastReceiver directoryReceiver;
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 创建广播接收器实例
        receiver = new NetworkChangeReceiver();

        // 注册广播接收器，监听网络状态变化
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(receiver, filter);

        // 创建 DirectoryBroadcastReceiver 实例

        directoryReceiver = new DirectoryBroadcastReceiver(){
            public void onReceive(Context context, Intent intent) {
                //Toast.makeText(context, "接收到的Intent的Action位："+intent.getAction()+"\n消息内容是："+intent.getIntExtra("directoryNumber",-1),Toast.LENGTH_LONG).show();
                if (intent.getAction().equals("com.example.lab1.DIRECTORY_ACTION")) {
                    int directoryNumber = intent.getIntExtra("directoryNumber", -1);
                    //Log.d("NavigateToNews", "Mainactivity directoryNumber: " + directoryNumber);
                    onNewsItemSelected(directoryNumber-1);
                }
            }
        };
        registerReceiver(directoryReceiver,new IntentFilter("com.example.lab1.DIRECTORY_ACTION"));

        // 初始化 NewsDetailFragment
        newsDetailFragment = (NewsDetailFragment) getSupportFragmentManager().findFragmentById(R.id.newsDetailFragment);

        if (newsDetailFragment == null) {
            newsDetailFragment = new NewsDetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, newsDetailFragment)
                    .commit();
        }

        NewsListFragment newsListFragment = (NewsListFragment) getSupportFragmentManager().findFragmentById(R.id.newsListFragment);
        newsListFragment.setOnNewsItemSelectedListener(this);
    }

    @Override
    public void onNewsItemSelected(int newsNumber) {
        String[] newsContents = new String[5];
        newsContents[0] = "               巴黎禁止在街头引入共享电动滑板车\n    在过去的几周里，由三家公司运营的约1.5万辆电动滑板车已被相继撤出，其中的大多数被重新投放到包括伦敦在内的其它欧洲城市继续使用。虽然巴黎街道上仍有很多电动滑板车，但这些电动滑板车是市民自购私人拥有的。巴黎是第一个将电动滑板车引入街头共享的欧洲城市，现在成为了第一个禁止这类交通工具出租的首都城市，这个试行计划只持续了五年。对一些巴黎市民来说，这项禁令让人倍感解脱，因为这种交通工具带来的麻烦远远超过了它们的用处。对另一些人来说，比如游客或玩乐后晚上想快速回家的人们，这项禁令则让人惋惜。运营商表示对这项禁令感到失望，但会将业务重心转向仍然合法的自由取还模式的共享电动自行车。";
        newsContents[1] = "         研究显示英国本土植物数量大幅下降\n    一项大型植物调查研究显示，对于70年前长大的人来说，如今英国的部分植物景观已面目全非，无法辨识。二十年来，志愿者们对英国和爱尔兰一公里一公里地统计，记录下了所发现的植物群。由英国和爱尔兰植物学会编制，这本 “植物图集” 是英国有记录以来最完整的植物图集。这本图集中显示了植物群数量的灾难性减少。参与研究的科学家们表示，部分地区的景观已发生了巨大的变化，以至于70年前长大的人在今天很难认出这些景观。";
        newsContents[2] = "         研究证实音乐训练与认知能力间存在联系\n    一项新研究证实，儿时学习演奏乐器与日后思维能力的提高存在关联。研究发现，会演奏乐器的人在认知能力测试中的表现略优于没有演奏过乐器的人。长期以来，人们一直认为精通一种乐器所需的身体和思维灵敏度与认知能力的提高有关。但现在，爱丁堡大学的研究人员们已经证明精通乐器还有可能延缓与衰老相关的智力衰退。该团队对英国洛锡安地区出生人口队列中的400名调查对象进行了测试，这个长期追踪研究组于1936年设立，跟踪研究发现在目前70多岁和80岁出头的人群中，儿时学过乐器的人大脑处理信息的速度和视觉空间推理方面的表现都更好。";
        newsContents[3] = "         研究表明每天走五千步即足以改善健康\n    长久以来，人们一直认为要保持身体健康，需要达成 “每天一万步” 这个 “神奇的” 目标，但一项新的研究表明，每天走不到5000步可能就足以给健康带来好处。多年来，成千上万的人不停地奔走，试图达到 “每天一万步” 这个被视为有奇妙功效的目标，从而保持身体健康，但一项新的研究发现，每天走路的步数只要达到这个数字的一半就有益于生命健康。为完成研究，波兰罗兹医科大学和美国约翰霍普金斯大学医学院的研究人员们对22.7万人进行了为期七年的跟踪调查。他们发现，每天至少走2300步对心脏和血管都有很大的好处，而且走得越多，死于心血管疾病的风险就越低。";
        newsContents[4] = "         英格兰大学质量欠佳的专业课程或被限制招生人数\n    在政府的新计划下，英格兰的大学里开设的质量欠佳的专业课程可能被限制招募学生。英国政府表示，这类无法为学生提供 “令人满意的结果” 的课程包括辍学率高和学生就业率低的课程。在这些计划下，英国学生事务办公室将被要求限制各大学提供的辍学率高或没能帮助毕业生找到好工作的专业课程可招收的学生人数。该监管机构已有权在学位课程的效果低于他们设定的最低表现阈值的情况下对大学进行调查和处罚。该阈值标准包括最少有75%的学生完成课程，以及至少60%的学生在毕业后的一年零三个月内继续学习深造或找到高技能工作。";
        if (newsDetailFragment != null) {
            if (newsNumber >= 0 && newsNumber < newsContents.length) {
                Log.d("NavigateToNews", "Updating news content for directoryNumber: " + newsNumber);
                newsDetailFragment.updateNewsContent(newsContents[newsNumber]);
            } else {
                // 显示提示消息
                showToast("请输入有效的目录值！");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在活动销毁时取消注册广播接收器
        this.unregisterReceiver(receiver);
        this.unregisterReceiver(directoryReceiver);
    }


}
