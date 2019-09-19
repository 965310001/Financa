//package tech.com.commoncore.utils;
//
///**
// * 倒计时工具类
// */
//public class RxCountdown {
//
//
//    public void startTimer(final int delay,final OnOverListener listener){
//        final Handler handler = new Handler(mContext.getMainLooper());
//        Runnable runnable = new Runnable(){
//            private int count = 0;
//            @Override
//            public void run() {
//                count++;
//                if(count < delay){
//                    listener.onTick(delay - count);
//                    handler.postDelayed(this,1000);
//                }else{
//                    listener.onOver();
//                }
//            }
//        };
//        handler.post(runnable);
//    }
//}
