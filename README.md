# kotlin-firestore-sample

## 手順メモ
次やること
⑤FeedFragmentでいいね出来るようにする
　→FeedViewModelでのいいねの状態



## 後でやるメモ
①通信中にインジゲーターを表示する
　→プロフィール更新中
　→

## 実装メモ
⑤現在はFeedAdapterでTweetモデルを利用しているので、ここを改変する
　→コメント数といいねしたかの状態を持つFeedモデルを導入する
　→それをBindingして、状態管理を行う
　???いいねした時の処理をどう処理するか
　   →AdapterにViewModelを渡して初期化させる
　　　→viewHolderの中でクリックした際にViewModel側に検知させて、更新を行う
　　　　→ViewModel



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
  →結構、すぐ出来る
  →ViewPagerでタブを利用する
　


## メモ
分からない＆あとで調べるメモ
①Taskや非同期処理が良くわかってない
　→処理の仕組みと、分離の仕方


