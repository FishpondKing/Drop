package com.fishpondking.android.drop.engine;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.fishpondking.android.drop.fragment.DiaryFragment;
import com.fishpondking.android.drop.fragment.MoneyFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: FishpondKing
 * Date: 2017/3/15:20:06
 * Email: song511653502@gmail.com
 * Description:
 */

public class MoneyEventLab {

    private static MoneyEventLab sMoneyEventLab;

    private static ArrayList<MoneyEvent> mMoneyEvents;

    private static int totalItemCount;
    private static int previousItemCount;

    public static MoneyEventLab get(Context context) {
        if (sMoneyEventLab == null) {
            sMoneyEventLab = new MoneyEventLab(context);
        }
        return sMoneyEventLab;
    }

    private MoneyEventLab(Context context) {
        mMoneyEvents = new ArrayList<MoneyEvent>();
    }

    //上拉加载MoneyEventLab
    public void pullUpRefreshMoneyEventLab(final Handler handler) {
        SingletonDormitory singletonDormitory = SingletonDormitory.getInstance();
        //获取此宿舍的账单
        AVQuery<AVObject> query = new AVQuery<>("MoneyEvent");
        query.orderByDescending("createdAt");
        query.whereEqualTo("dormitoryId", singletonDormitory.getId());
        query.findInBackground(new FindCallback<AVObject>() {
                                   @Override
                                   public void done(List<AVObject> list, AVException e) {
                                       //该宿舍没有MoneyEvent
                                       if (list.size() == 0) {
                                           return;
                                       }
                                       //获取已经加载的Diary的总数量
                                       if (mMoneyEvents == null) {
                                           totalItemCount = 0;
                                       } else {
                                           totalItemCount = mMoneyEvents.size();
                                       }
                                       previousItemCount = totalItemCount;

                                       //向UI线程发送MoneyEvent的总数量和已经加载的数量
                                       Message messageDiaryCount = new Message();
                                       messageDiaryCount.what = MoneyFragment.MONEY_EVENT_LIST_SIZE;
                                       messageDiaryCount.arg1 = list.size();
                                       messageDiaryCount.arg2 = previousItemCount;
                                       handler.sendMessage(messageDiaryCount);

                                       //加载每一条MoneyEvent
                                       if (totalItemCount < list.size()) {
                                           int i = 0;
                                           while (i < MoneyFragment.EACH_PAGE_MONEY_EVENT_COUNT && totalItemCount < list.size()) {
                                               //加载每一篇MoneyEvent
                                               AVObject avMoneyEvent = (AVObject) list.get(previousItemCount + i);
                                               //AVObject avMoneyEvent = (AVObject) list.get(previousItemCount);
                                               MoneyEvent moneyEvent = new MoneyEvent();
                                               moneyEvent.setDormitoryId(avMoneyEvent.getString("dormitoryId"));
                                               moneyEvent.setEventType(avMoneyEvent.getInt("eventType"));
                                               moneyEvent.setReason(avMoneyEvent.getString("reason"));
                                               moneyEvent.setCount(Double.valueOf(avMoneyEvent.getString("eventCount")));
                                               moneyEvent.setDate(avMoneyEvent.getDate("createdAt"));
                                               mMoneyEvents.add(moneyEvent);

                                               Message messageAddMoneyEvent = new Message();
                                               messageAddMoneyEvent.what = MoneyFragment.ONE_MONEY_EVENT_DOWNLOAD_SUCCESS;
                                               handler.sendMessage(messageAddMoneyEvent);

                                               totalItemCount++;
                                               i++;
                                           }
                                       }
                                   }
                               }

        );
    }

    //下拉刷新MoneyEventLab

    public void pullDownRefreshMoneyEventLab(final Handler handler) {
        mMoneyEvents.clear();
        Message msg = new Message();
        msg.what = MoneyFragment.REQUEST_REFRESH_MONEY_EVENT_LIST;
        handler.sendMessage(msg);
        pullUpRefreshMoneyEventLab(handler);
    }

    //获取MoneyEvent列表
    public ArrayList<MoneyEvent> getMoneyEvents(Handler handler) {
        if (mMoneyEvents == null || mMoneyEvents.isEmpty()) {
            pullDownRefreshMoneyEventLab(handler);
            return mMoneyEvents;
        } else {
            return getMoneyEvents();
        }
    }

    public ArrayList<MoneyEvent> getMoneyEvents() {
        return mMoneyEvents;
    }

}
