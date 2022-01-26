package pinacolada.cards.pcl.colorless;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.KaedeAkamatsu;
import pinacolada.powers.PCLPower;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class ShuichiSaihara extends PCLCard
{
    public static final PCLCardData DATA = Register(ShuichiSaihara.class).SetSkill(0, CardRarity.UNCOMMON, PCLCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Danganronpa)
            .PostInitialize(data -> data.AddPreview(new KaedeAkamatsu(), false));

    public ShuichiSaihara()
    {
        super(DATA);

        Initialize(0, 0, 0, 0);
        SetExhaust(true);

        SetAffinity_Blue(1);
        SetProtagonist(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        CardGroup[] groups = upgraded ? (new CardGroup[] {p.discardPile, p.drawPile}) : (new CardGroup[] {p.discardPile});
        PCLActions.Bottom.FetchFromPile(name, 1, groups)
                .SetMessage(MoveCardsAction.TEXT[0])
                .SetOptions(false, false)
                .AddCallback(cards ->
                {
                    if (cards.size() > 0)
                    {
                        AbstractCard card = cards.get(0);
                        PCLActions.Bottom.Add(new RefreshHandLayout());
                        PCLActions.Bottom.ApplyPower(new ShuichiSaiharaPower(p, card)).AllowDuplicates(true);

                        if (PCLGameUtilities.IsHindrance(card) && info.TryActivateLimited()) {
                            PCLActions.Bottom.MakeCardInHand(new KaedeAkamatsu());
                        }
                    }
                });
    }

    public static class ShuichiSaiharaPower extends PCLPower
    {
        private AbstractCard card;
        public ShuichiSaiharaPower(AbstractPlayer owner, AbstractCard card)
        {
            super(owner, ShuichiSaihara.DATA);

            this.card = card;

            updateDescription();
        }

        @Override
        public void atEndOfTurn(boolean isPlayer) {
            super.atEndOfTurn(isPlayer);
            PCLActions.Bottom.Exhaust(card);
        }

        @Override
        public void onExhaust(AbstractCard card)
        {
            super.onExhaust(card);

            if (card.uuid.equals(this.card.uuid) && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {

                boolean shouldReduceCost = owner.hasPower(PCLPower.DeriveID(KaedeAkamatsu.DATA.ID));

                CardRarity r;
                switch (card.rarity) {
                    case UNCOMMON:
                        r = CardRarity.RARE;
                        break;
                    case SPECIAL:
                    case COMMON:
                        r = CardRarity.UNCOMMON;
                        break;
                    default:
                        r = CardRarity.COMMON;
                }
                final RandomizedList<AbstractCard> pool = PCLGameUtilities.GetCardPoolInCombat(r);
                final CardGroup choice = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

                while (choice.size() < 3 && pool.Size() > 0)
                {
                    AbstractCard temp = pool.Retrieve(rng);
                    if (!(temp.cardID.equals(card.cardID) || temp.tags.contains(AbstractCard.CardTags.HEALING) || temp.tags.contains(GR.Enums.CardTags.VOLATILE))) {
                        AbstractCard temp2 = temp.makeCopy();
                        if (shouldReduceCost) {
                            PCLGameUtilities.ModifyCostForCombat(temp2, -1, true);
                        }
                        choice.addToTop(temp2);
                    }
                }

                PCLActions.Bottom.SelectFromPile(name, 1, choice)
                        .SetOptions(false, false)
                        .AddCallback(cards ->
                        {
                            PCLActions.Bottom.MakeCardInDrawPile(cards.get(0));
                        });

                flashWithoutSound();
                this.RemovePower();
                if (shouldReduceCost) {
                    GameActions.Bottom.RemovePower(owner, owner, PCLPower.DeriveID(KaedeAkamatsu.DATA.ID));
                }
            }
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, PCLJUtils.ModifyString(card.name, w -> "#y" + w));
        }
    }
}