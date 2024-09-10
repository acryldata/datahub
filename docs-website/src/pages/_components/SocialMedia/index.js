import React, { useEffect, useRef } from "react";
import styles from "./social-media.module.scss";
import Test from "./Test";

const SocialMedia = () => {
  const secondCardRef = useRef(null);

  useEffect(() => {
    secondCardRef.current?.scrollIntoView({
      behavior: "smooth",
      block: "center",
    });
  }, []);
  return (
    <div className={styles.container}>
      <div className={styles.social_media}>
        <div className={styles.sm_left}>
          <div className={styles.sm_left_upper}>
            <div className={styles.sm_left_upper_left}>
              <div className={styles.sm_left_upper_leftcontent}>
                Built <b>for</b> Data Practitioners,
                <br /> <b>by</b> Data Practitioners
              </div>
            </div>
            <div className={styles.sm_left_upper_right}>
              <div className={styles.sm_left_upper_rightcontent}></div>
            </div>
          </div>
          <div className={styles.sm_left_lower}>
            <div className={styles.sm_left_lower_content}>
              <div className={styles.sm_left_lower_content_text}>
                Born at LinkedIn, driven by Acryl <br /> and 500+ community
                contributors.
              </div>
              <div className={styles.sm_left_lower_content_media_list}>
                <div className={styles.sm_left_lower_content_media_item}>
                  <div className={styles.sm_left_lower_content_media_icon}>
                    Y
                  </div>
                  <div className={styles.sm_left_lower_content_media_name}>
                    Youtube<span>2.9k subscribers</span>
                  </div>
                </div>
                <div className={styles.sm_left_lower_content_media_item}>
                  <div className={styles.sm_left_lower_content_media_icon}>
                    L
                  </div>
                  <div className={styles.sm_left_lower_content_media_name}>
                    LikedIn<span>1.9k subscribers</span>
                  </div>
                </div>
                <div className={styles.sm_left_lower_content_media_item}>
                  <div className={styles.sm_left_lower_content_media_icon}>
                    N
                  </div>
                  <div className={styles.sm_left_lower_content_media_name}>
                    Newsletters<span>1.3k subscribers</span>
                  </div>
                </div>
                <div className={styles.sm_left_lower_content_media_item}>
                  <div className={styles.sm_left_lower_content_media_icon}>
                    M
                  </div>
                  <div className={styles.sm_left_lower_content_media_name}>
                    Medium<span>1k subscribers</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className={styles.sm_right}>
          <div className={styles.sm_right_card_list}>
            <div className={styles.sm_right_card}>Card-A</div>
            <div className={styles.sm_right_card} ref={secondCardRef}>
              Card-B
            </div>
            <div className={styles.sm_right_card}>Card-C</div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SocialMedia;
