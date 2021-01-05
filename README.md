# kotlin-firestore-sample

## 手順メモ
次やること
⑥プロフィールページの仕上げを行う
　→ユーザーの投稿＋いいねした投稿
　→タブで切り替え出来る様にする
　→まず、自分のプロフィールページから行う　


## 実装メモ
⑥ProfileFragmentでの処理
　問題：初回のProfileFragmentでは、投稿＆いいねが表示されるが、2回目以降は表示されない
　　　　エラーは出ていない
　仮説
　①Fragmentが初期化されずに、空として扱われている
　　→多分、これ
　　→解決

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
　→ここが1番の難所？？？
⑥プロフィールページでユーザーの投稿＋いいねした投稿を取得する
  →結構、すぐ出来る
  →ViewPagerでタブを利用する
　


## メモ
分からない＆あとで調べるメモ
①Taskや非同期処理が良くわかってない
　→処理の仕組みと、分離の仕方


