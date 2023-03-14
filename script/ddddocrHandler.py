import ddddocr
import sys
from PIL import ImageFile

ImageFile.LOAD_TRUNCATED_IMAGES = True

def getText(filepath):
    ocr = ddddocr.DdddOcr(beta=True,show_ad=False)

    with open(filepath, 'rb') as f:
        image = f.read()

    res = ocr.classification(image)
    print(res)

def img_to_text(filepath):
    ocr = ddddocr.DdddOcr(beta=True,show_ad=False)
    with open(filepath,'rb') as f:
        img_bytes = f.read()
    res = ocr.classification(img_bytes)
    print(res)

if __name__== "__main__":
    # getText(sys.argv[1])
    # img_to_text(r"D:\Burp_Suite_Pro_v2021.5.1_Loader_Keygen\1678779106187mgr2.kredinesia.id.burp-api.jpg")
    img_to_text(sys.argv[1])