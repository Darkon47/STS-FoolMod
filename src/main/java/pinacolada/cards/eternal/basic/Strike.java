package pinacolada.cards.eternal.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class Strike extends EternalCard
{
    public static final PCLCardData DATA = Register(Strike.class).SetAttack(1, CardRarity.BASIC);

    public Strike()
    {
        super(DATA);

        Initialize(6, 0, 3);
        SetUpgrade(3, 0, 0);

        this.cropPortrait = false;
        this.tags.add(CardTags.STARTER_STRIKE);
        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HORIZONTAL);
    }
}