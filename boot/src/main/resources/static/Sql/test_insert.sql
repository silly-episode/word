create
    definer = root@localhost procedure test_insert()
BEGIN

    DECLARE y BIGINT DEFAULT 1;

    WHILE y<1000

        DO

            INSERT INTO `book_of_words` (`book_id`, `word`, `meaning`, `ukphone`) VALUES ('1', 'word', '单词', 'products');

            SET y=y+1;

        END WHILE ;

    commit;

END;

