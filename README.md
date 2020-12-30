# kotlin-firestore-sample

## 手順メモ
次やること
⑤いいね(FeedFragment内で)&いいね＋コメント(Details内で)
　→いいね処理を行う
　→Adapterの処理を進める
　→コメントの表示＆コメント投稿を行う。

## 後でやるメモ
①通信中にインジゲーターを表示する
　→プロフィール更新中
　→

## 実装メモ
⑤
1.FeedFragmentから移動する
　→詳細ページの表示を行う
2.TweetDetailViewModel＆UI作成
　→そこで、データの変更を感知する？
3.いいね＆Feedでデータ変更を行う
4.コメントのためのRecyclerViewの設定を行う
　→UI・ViewModel・Repository
5.コメントを行う


①ユーザー一覧を作成する
　→ユーザーセルを作成
　→RecyclerViewで対応＆SearchViewで検索できるようにする
②プロファイル画面を作成する
　→自分のユーザーの情報を表示する
　→ProfileFragmentからsafe argsでviewModelを渡して、その場で遷移できるようにする
③他のユーザーのプロフィール画面を作成＆フォローできるようにする
　→SearchFragmentからsafe argsでUserを渡して遷移する
④Tweetできるようにする
　→Tweet用のActivity作成する。
　→ボタンを押したら遷移する。
　→そこで投稿もしくはキャンセルしたら一覧画面に戻る
⑤Tweet詳細画面作作成＆いいね・コメントできるようにする
⑥プロフィールページでユーザーの投稿＋いいねした投稿を取得する
　


## メモ
分からない＆あとで調べるメモ
①Taskや非同期処理が良くわかってない
　→処理の仕組みと、分離の仕方


